package dev.urieloalves.usecase.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.infrastructure.shared.Env
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.user.dto.InputGenerateTokenUseCaseDto
import dev.urieloalves.usecase.user.dto.OutputGenerateTokenUseCaseDto
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
            val msg = "Could not generate JWT token for user `${input.userId}`"
            logger.error(msg, e)
            throw ServerException(
                message = msg,
                cause = e
            )
        }
    }
}