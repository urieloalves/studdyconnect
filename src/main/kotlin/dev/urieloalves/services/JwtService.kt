package dev.urieloalves.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.configs.Env
import dev.urieloalves.data.models.CustomException
import java.time.Instant

interface JwtService {
    fun generateToken(userId: String): String
}

class JwtServiceImpl : JwtService {

    override fun generateToken(userId: String): String {
        try {
            return JWT.create()
                .withClaim("id", userId)
                .withExpiresAt(Instant.now().plusSeconds(Env.JWT_EXPIRES_IN_MINUTES * 60))
                .sign(Algorithm.HMAC256(Env.JWT_SECRET))
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not generate JWT token for user `$userId`",
                cause = e
            )
        }

    }
}