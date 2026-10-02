package com.mashiverse.mashit.app.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mashiverse.mashit.data.local.db.RoomDb
import com.mashiverse.mashit.data.local.db.createDb
import com.mashiverse.mashit.data.local.db.daos.MashupDao
import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.ds.createDatastore
import org.koin.core.module.Module
import org.koin.dsl.module

val localModule = module {
    single<DataStore<Preferences>> { createDatastore(get()) }

    single<RoomDb> { createDb(get()) }

    factory<NftDao> { get<RoomDb>().getNftDao() }

    factory<TraitTypeDao> { get<RoomDb>().getImageTypeDao() }

    factory<MashupDao> { get<RoomDb>().getMashupDao() }
}

expect val dbContextModule: Module