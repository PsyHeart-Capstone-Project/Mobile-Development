package com.capstone.psyheart.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.di.Injection
import com.capstone.psyheart.ui.discover.DiscoverViewModel
import com.capstone.psyheart.ui.discover_detail.DiscoverDetailViewModel
import com.capstone.psyheart.ui.login.LoginViewModel
import com.capstone.psyheart.ui.register.RegisterViewModel


class ViewModelFactory(
    private val userRepository: UserRepository,
    private val songRepository: SongRepository

) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(DiscoverViewModel::class.java) -> {
                DiscoverViewModel(songRepository) as T
            }

            modelClass.isAssignableFrom(DiscoverDetailViewModel::class.java) -> {
                DiscoverDetailViewModel(songRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideSongRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}