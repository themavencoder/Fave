package com.example.breezil.fave.ui.preference

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View

import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.ActivityAboutBinding
import com.example.breezil.fave.ui.BaseActivity
import com.example.breezil.fave.utils.Constant.Companion.ABOUT

import java.util.Calendar

import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutActivity : BaseActivity() {

    lateinit var binding: ActivityAboutBinding

    private val copyRights: Element
        get() {
            val copyRightsElement = Element()
            val copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR))
            copyRightsElement.title = copyrights
            copyRightsElement.iconDrawable = R.drawable.ic_copyright_black_24dp
            copyRightsElement.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
            copyRightsElement.iconNightTint = android.R.color.white
            copyRightsElement.gravity = Gravity.CENTER
            return copyRightsElement
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)

        supportActionBar!!.title = ABOUT
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val aboutPage = createPage()
        binding.aboutLayout.addView(aboutPage, 0)
    }

    private fun createPage(): View {
        return AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.mipmap.ic_launcher_round)
                .addItem(Element().setTitle(String.format(getString(R.string.version))))
                .addGroup(getString(R.string.contacts))
                .addEmail(getString(R.string.email), getString(R.string.email_title))
                .addWebsite(getString(R.string.web), getString(R.string.website))
                .addTwitter(getString(R.string.twitter), getString(R.string.ontwitter))
                .addGitHub(getString(R.string.github), getString(R.string.ongithub))
                .addItem(copyRights)
                .create()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
