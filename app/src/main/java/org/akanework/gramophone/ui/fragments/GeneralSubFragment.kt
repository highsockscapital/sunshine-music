/*
 *     Copyright (C) 2024 Akane Foundation
 *
 *     Gramophone is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gramophone is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.akanework.gramophone.ui.fragments

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.akanework.gramophone.R
import org.akanework.gramophone.logic.enableEdgeToEdgePaddingListener
import org.akanework.gramophone.logic.ui.MyRecyclerView
import org.akanework.gramophone.logic.utils.flows.PauseManagingSharedFlow.Companion.sharePauseableIn
import org.akanework.gramophone.logic.utils.flows.provideReplayCacheInvalidationManager
import org.akanework.gramophone.ui.adapters.SongAdapter
import org.akanework.gramophone.ui.adapters.Sorter
import uk.akane.libphonograph.dynamicitem.Favorite
import uk.akane.libphonograph.dynamicitem.RecentlyAdded
import uk.akane.libphonograph.items.Playlist
import uk.akane.libphonograph.manipulator.PlaylistSerializer.Entry

/**
 * GeneralSubFragment:
 *   Inherited from [BaseFragment]. Sub fragment of all
 * possible item types. TODO: Artist / AlbumArtist
 *
 * @see BaseFragment
 * @author AkaneTan, nift4
 */
class GeneralSubFragment : BaseFragment(true) {
    companion object {
        private const val TAG = "GeneralSubFragment"
    }

    private var songListJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        lateinit var itemList: Flow<List<MediaItem>?>

        val rootView = inflater.inflate(R.layout.fragment_general_sub, container, false)
        val topAppBar = rootView.findViewById<MaterialToolbar>(R.id.topAppBar)
        val collapsingToolbarLayout =
            rootView.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbar)
        val recyclerView = rootView.findViewById<MyRecyclerView>(R.id.recyclerview)
        val appBarLayout = rootView.findViewById<AppBarLayout>(R.id.appbarlayout)
        appBarLayout.enableEdgeToEdgePaddingListener()

        val bundle = requireArguments()
        val itemType = bundle.getInt("Item")
        val id = bundle.getString("Id")?.toLong()

        val title: Flow<String>
        var rawOrderExposed: Sorter.Type? = null
        var isPlainPlaylist = false

        when (itemType) {
            R.id.album -> {
                val item = mainActivity.reader.albumListFlow.map { it.find { it.id == id } }
                title = item.map { it?.title ?: requireContext().getString(R.string.unknown_album) }
                itemList = item.map { it?.songList }
                rawOrderExposed = Sorter.Type.ByAlbumTitleAscending
            }

            /*R.id.artist -> {
                val item = libraryViewModel.artistItemList.value!![position]
                title = item.title ?: requireContext().getString(R.string.unknown_artist)
                itemList = item.songList
            } TODO */

            R.id.genre -> {
                // Genres
                val item = mainActivity.reader.genreListFlow.map { it.find { it.id == id } }
                title = item.map { it?.title ?: requireContext().getString(R.string.unknown_genre) }
                itemList = item.map { it?.songList }
            }

            R.id.date -> {
                // Dates
                val item = mainActivity.reader.dateListFlow.map { it.find { it.id == id } }
                title = item.map { it?.title ?: requireContext().getString(R.string.unknown_year) }
                itemList = item.map { it?.songList }
            }

            /*R.id.album_artist -> {
                // Album artists
                val item = libraryViewModel.albumArtistItemList.value!![position]
                title = item.title ?: requireContext().getString(R.string.unknown_artist)
                itemList = item.songList
            } TODO */

            R.id.playlist -> {
                // Playlists
                val clazz = arguments?.getString("Class") ?: "null"
                val item = mainActivity.reader.playlistListFlow.map {
                    it.find { if (id != null) it.id == id else it.javaClass.name == clazz }
                }
                    .provideReplayCacheInvalidationManager()
                    .sharePauseableIn(
                        CoroutineScope(Dispatchers.Default),
                        WhileSubscribed(),
                        replay = 1
                    )
                title = item.map {
                    if (it is RecentlyAdded) {
                        requireContext().getString(R.string.recently_added)
                    } else if (it is Favorite) {
                        requireContext().getString(R.string.playlist_favourite)
                    } else {
                        it?.title ?: (requireContext().getString(R.string.unknown_playlist)
                                + if (it != null) " (${it.id} - ${it.path})" else "")
                    }
                }
                itemList = item.map { it?.songList }
                rawOrderExposed = Sorter.Type.NaturalOrder
                // Sunshine Music: batch add lives on the playlist screen only, and is wired up
                // after the adapter exists. See setUpPlaylistBatchAdd.
                isPlainPlaylist = clazz == Playlist::class.java.name
            }

            else -> throw IllegalArgumentException()
        }

        val sharedTitle = title.sharePauseableIn(lifecycleScope + Dispatchers.Default,
            WhileSubscribed(), replay = 1)
        lifecycleScope.launch(Dispatchers.Default) {
            sharedTitle.collect {
                withContext(Dispatchers.Main) {
                    // Show title text.
                    collapsingToolbarLayout.title = it
                }
            }
        }

        val songAdapter =
            SongAdapter(
                this,
                sharedTitle,
                itemList,
                rawOrderExposed = rawOrderExposed,
                isSubFragment = itemType
            )

        recyclerView.enableEdgeToEdgePaddingListener()
        recyclerView.setAppBar(appBarLayout)
        recyclerView.adapter = songAdapter.concatAdapter

        // Build FastScroller.
        recyclerView.fastScroll(songAdapter, songAdapter.itemHeightHelper)

        if (isPlainPlaylist) {
            setUpPlaylistBatchAdd(
                songAdapter, topAppBar, collapsingToolbarLayout, id, itemList
            )
        }

        topAppBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return rootView
    }

    /**
     * Sunshine Music: mark songs in this playlist and add them somewhere else in one go.
     *
     * Order is: choose the destination first, then mark. That is not a preference, it is what
     * makes the duplicate guard possible. Once the destination is known, the songs it already
     * holds are shown ticked and cannot be unticked, so a second run against the same playlist
     * cannot add anything twice and the user can see what is already there without guessing.
     * Choosing the destination last would leave the screen with no way to tell which songs are
     * already in there.
     *
     * The marked count goes in the collapsing toolbar subtitle rather than into the action title,
     * because the action has to keep saying "Add to playlist" to stay unambiguous, and the title
     * is already bound to the playlist name by the title flow.
     *
     * The action is hidden entirely for an empty playlist: there is nothing to mark, and a
     * button that leads to an empty list is worse than no button.
     */
    /**
     * The toolbar is passed in rather than looked up. This runs from onCreateView, and the
     * fragment's view is not published until onCreateView returns, so requireView() throws
     * "did not return a View from onCreateView()" right here. onCreateView already holds the
     * real, inflated toolbar, so there is no reason to go back through the fragment for it.
     */
    private fun setUpPlaylistBatchAdd(
        adapter: SongAdapter,
        topAppBar: MaterialToolbar,
        collapsingToolbarLayout: CollapsingToolbarLayout,
        playlistId: Long?,
        songList: Flow<List<MediaItem>?>
    ) {
        topAppBar.inflateMenu(R.menu.playlist_subfragment_menu)
        val menu = topAppBar.menu
        val addItem = menu.findItem(R.id.add_to_playlist)
        var destination: Playlist? = null

        fun render(count: Int) {
            val selecting = adapter.isSelecting()
            addItem?.isEnabled = count > 0
            menu.findItem(R.id.edit)?.isVisible = !selecting
            menu.findItem(R.id.cancel_selection)?.isVisible = selecting
            collapsingToolbarLayout.subtitle = if (selecting && count > 0) {
                resources.getQuantityString(R.plurals.songs_selected, count, count)
            } else null
        }

        adapter.onSelectionChanged = { count -> render(count) }

        // Hide the action until there is at least one song to mark. Collects the same flow the
        // adapter does, which is a shared flow with a replay cache, so this does not re-query.
        songListJob = lifecycleScope.launch {
            songList.collect { songs ->
                if (!adapter.isSelecting()) {
                    addItem?.isVisible = !songs.isNullOrEmpty()
                }
            }
        }

        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    mainActivity.startFragment(PlaylistEditFragment()) {
                        putString("Id", playlistId?.toString())
                    }
                    true
                }

                R.id.add_to_playlist -> {
                    val picked = destination
                    if (picked != null && adapter.isSelecting()) {
                        val chosen = adapter.getSelectedItems()
                        if (chosen.isNotEmpty()) {
                            mainActivity.addToPlaylist(playlistUriOf(picked), null, entriesOf(chosen))
                        }
                        adapter.endSelection()
                    } else {
                        // Step one: destination.
                        mainActivity.pickPlaylistDialog(playlistId) { chosen ->
                            if (chosen == null) return@pickPlaylistDialog
                            destination = chosen
                            // chosen.songList is a plain, already-materialised List<MediaItem>:
                            // Playlist.toPlaylist builds every playlist the chooser hands over
                            // through the public constructor, with its songs in place. So there is
                            // nothing to await here, no coroutine, and no flow.
                            //
                            // Two earlier versions of this line were wrong. "first() ?: emptyList()"
                            // and then "first().orEmpty()" both treated songList as a Flow, when
                            // it is a plain property, so first() was resolving to List.first() and
                            // handing back a single MediaItem. Read the declaration, not the name.
                            adapter.beginSelection(chosen.songList)
                        }
                    }
                    true
                }

                R.id.cancel_selection -> {
                    destination = null
                    adapter.endSelection()
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        // That collector closes over the toolbar and the adapter, both of which come from the
        // view being torn down. The fragment itself can outlive its view, for instance on the back
        // stack, and the fragment scope would keep the collector running against dead views.
        songListJob?.cancel()
        songListJob = null
        super.onDestroyView()
    }

    private fun playlistUriOf(playlist: Playlist): Uri = ContentUris.withAppendedId(
        @Suppress("deprecation") MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
        playlist.id!!
    )

    private fun entriesOf(items: List<MediaItem>): List<Entry> =
        items.mapNotNull { Entry.ofMediaItem(it) }
}
