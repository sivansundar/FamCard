package com.sivan.famcard.util.sharedpref

import com.sivan.famcard.domain.DismissedId

interface SharedPrefUtilHelper {

    fun saveDismissedId(id : DismissedId)
    fun getAllDismissedIds() : ArrayList<DismissedId>

}