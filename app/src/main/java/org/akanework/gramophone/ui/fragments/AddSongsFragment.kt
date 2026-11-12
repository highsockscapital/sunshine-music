/*
 *     Copyright (C) 2026 The Gramophone authors
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

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.akanework.gramophone.R
import org.akanework.gramophone.logic.enableEdgeToEdgePaddingListener
import org.akanework.gramophone.logic.ui.MyRecyclerView
import org.akanework.gramophone.ui.adapters.SongAdapter
import org.akanework.gramophone.ui.adapters.Sorter
import uk.akane.libphonograph.manipulator.PlaylistSerializer.Entry

/**
 * Sunshine Music: fill a playlist with songs, rather than sending songs out to one.
 *
 * The playlist screen's batch action copies songs *out* of the playlist being viewed. That left
 * creating a playlist as a dead end: the name dialog produced an empty playlist, and an empty
 * playlist has no songs to mark, so there was no way to put anything into it without leaving,
 * opening the songs tab, marking, choosing a destination. This screen is the missing direction:
 * it lists the whole library, marks songs, and appends them to one known playlist.
 *
 * It is deliberately a full screen rather than a dialog because picking songs is the same browse
 * and listen task the rest of the app already does, and the row checkbox pattern is the one the
 * playlist screen already uses. Songs the target playlist already holds start ticked and cannot
 * be unticked, so a second visit cannot add the same song twice by accident.
 */
class AddSongsFragment : BaseFragment(false) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_general_sub, container, false)
        val topAppBar = rootView.findViewById<MaterialToolbar>(R.id.topAppBar)
        val collapsingToolbarLayout =
            rootView.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbar)
        val recyclerView = rootView.findViewById<MyRecyclerView>(R.id.recyclerview)
        val appBarLayout = rootView.findViewById<AppBarLayout>(R.id.appbarlayout)
        appBarLayout.enableEdgeToEdgePaddingListener()

        val bundle = requireArguments()
        val uri = Uri.parse(bundle.getString("Uri")!!)
        val title = bundle.getString("Title")
        val playlistId = bundle.getString("Id")?.toLongOrNull()

        val adapter = SongAdapter(
            this,
            null,
            rawOrderExposed = Sorter.Type.ByTitleAscending,
            // R.id.songs makes this read as a pure picker: no per-row overflow menu, and the
            // row click does not start playback. Selection is switched on below before the
            // list is ever attached, so the adapter never reaches its non-selecting click path.
            isSubFragment = R.id.songs,
        )

        collapsingToolbarLayout.title = title
            ?: getString(R.string.add_songs_to_this_playlist)

        topAppBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        topAppBar.inflateMenu(R.menu.add_songs_menu)
        val confirmItem = topAppBar.menu.findItem(R.id.confirm_add_songs)

        fun render(count: Int) {
            collapsingToolbarLayout.subtitle = if (count > 0) {
                resources.getQuantityString(R.plurals.songs_selected, count, count)
            } else {
                getString(R.string.select_songs_hint)
            }
            confirmItem?.isEnabled = count > 0
        }

        adapter.onSelectionChanged = { count -> render(count) }

        recyclerView.enableEdgeToEdgePaddingListener()
        recyclerView.setAppBar(appBarLayout)
        recyclerView.adapter = adapter.concatAdapter

        // Build FastScroller.
        recyclerView.fastScroll(adapter, adapter.itemHeightHelper)

        // Start in selection mode immediately, before the first bind, so there is never a frame
        // where a tap could start playback instead of marking a song.
        adapter.beginSelection()
        render(0)

        // Which songs the target already holds is read off the library flow, which may suspend,
        // so it cannot be known before the screen is usable. Marking starts empty and is filled
        // in when the playlist resolves; a song marked present cannot be unticked or counted.
        if (playlistId != null) {
            lifecycleScope.launch {
                val existing = withContext(Dispatchers.Default) {
                    mainActivity.reader.playlistListFlow.first()
                        .find { it.id == playlistId }?.songList.orEmpty()
                }
                adapter.updateAlreadyPresent(existing)
            }
        }

        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.confirm_add_songs -> {
                    val chosen = adapter.getSelectedItems()
                    if (chosen.isNotEmpty()) {
                        mainActivity.addToPlaylist(
                            uri, null, chosen.mapNotNull { Entry.ofMediaItem(it) }
                        )
                    }
                    requireActivity().supportFragmentManager.popBackStack()
                    true
                }

                else -> false
            }
        }

        return rootView
    }
}
