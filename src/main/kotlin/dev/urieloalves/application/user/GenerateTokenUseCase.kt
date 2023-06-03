package dev.urieloalves.application.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.application.user.dto.InputGenerateTokenUseCaseDto
import dev.urieloalves.application.user.dto.OutputGenerateTokenUseCaseDto
import dev.urieloalves.infrastructure.shared.Env
import org.slf4j.LoggerFactory
import java.time.Instant

class GenerateTokenUseCase(
    private val jwtSecret: String = Env.JWT_SECRET,
    private val expiresInMinutes: Long = Env.JWT_EXPIRES_IN_MINUTES
) {

    private val logger = LoggerFactory.getLogger("GenerateTokenUseCase")

    fun execute(input: InputGenerateTokenUseCaseDto): OutputGenerateTokenUseCaseDto {
        try {
            logger.info("Generating token for user '${input.userId}'")
            val token = JWT.create()
                .withClaim("id", input.userId.toString())
                .withExpiresAt(Instant.now().plusSeconds(expiresInMinutes * 60))
                .sign(Algorithm.HMAC256(jwtSecret))
            return OutputGenerateTokenUseCaseDto(
                token = token
            )
        } catch (e: Exception) {
            logger.error("Could not generate JWT token for user `${input.userId}`", e)
            throw e
        }
    }
}