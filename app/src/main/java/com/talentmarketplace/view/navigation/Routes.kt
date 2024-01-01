package com.talentmarketplace.view.navigation

// Only classes within sealed class can inherit it's properties
sealed class Routes(val route: String) {
    sealed class Auth(route: String): Routes(route) {
        object SignUp : Auth("auth/signUp")
        object SignIn : Auth("auth/signIn")
        object SignOut : Auth("auth/signOut")
        object GoogleSignIn : Auth("auth/googleSignIn")
    }

    sealed class Job(route: String): Routes(route) {
        object List: Job("jobs/")
        object Get: Job("jobs/{id}") {
            fun byID(id: String?) = "jobs/$id"
            fun map(id: String?) = "jobs/$id/map"
        }

    }

    sealed class User(route: String): Routes(route) {
        object Profile: User("user/profile")
        object Settings: User("user/settings")
    }
}