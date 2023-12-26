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
        object List: Job("job/list")
        object Create: Job("job/create")
        object Get: Job("job/get/{id}") {
            fun byID(id: String) = "job/get/$id"
        }
    }

    sealed class User(route: String): Routes(route) {
        object Profile: User("user/profile")
        object Settings: User("user/settings")
    }
}