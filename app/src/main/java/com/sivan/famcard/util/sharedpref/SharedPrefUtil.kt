package com.sivan.famcard.util.sharedpref

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sivan.famcard.domain.DismissedId
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefUtil @Inject constructor(@ApplicationContext context: Context) :
    SharedPrefUtilHelper {

    val prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    override fun saveDismissedId(id: DismissedId) {
        val idObject = getAllDismissedIds()
        idObject.add(id)
        val idToJsonString = Gson().toJson(idObject)
        prefs.edit().putString(DISMISSED_IDS, idToJsonString).apply()
    }

    override fun getAllDismissedIds(): ArrayList<DismissedId> {
        val json: String? = prefs.getString(DISMISSED_IDS, null)
        val type = object : TypeToken<ArrayList<DismissedId>>() {}.type
        return Gson().fromJson(json, type)
    }

    companion object {
        private val PREF_FILE = "FAM_CARD_APP"
        const val DISMISSED_IDS = "dismissed_ids"

    }
}