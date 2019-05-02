package com.fave.breezil.fave.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

import com.fave.breezil.fave.R
import com.fave.breezil.fave.ui.main.fragment.BookMarksFragment
import com.fave.breezil.fave.ui.main.fragment.HeadlinesFragment
import com.fave.breezil.fave.ui.main.fragment.PreferredFragment


class PagerAdapter(fm: FragmentManager, internal var context: Context) : FragmentStatePagerAdapter(fm) {
    private var fragmentList = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            preferred_Position -> PreferredFragment()
            headline_Position -> HeadlinesFragment()
            else -> BookMarksFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val resources = context.resources
        return when (position) {
            preferred_Position -> resources.getString(R.string.preferred_fragment)

            headline_Position -> resources.getString(R.string.head_line_fragment)
            bookmark_Position -> resources.getString(R.string.book_mark_fragment)
            else -> "none"
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fragmentList.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fragmentList.remove(position)
        super.destroyItem(container, position, `object`)
    }

    companion object {

        private const val preferred_Position = 0
        private const val headline_Position = 1
        private const val bookmark_Position = 2
    }
}
