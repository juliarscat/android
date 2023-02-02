// code to generate a secure password

import java.security.SecureRandom
import java.util.*

fun main() {
    val secureRandom = SecureRandom()
    val password = generatePassword(secureRandom, 20)
    println("Generated password: $password")
}

fun generatePassword(secureRandom: SecureRandom, length: Int): String {
    val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{};:'\"\\|,.<>/?`~"
    val password = StringBuilder(length)
    for (i in 0 until length) {
        password.append(characters[secureRandom.nextInt(characters.length)])
    }
    return password.toString()
}
