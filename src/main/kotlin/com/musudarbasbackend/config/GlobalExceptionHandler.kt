package com.musudarbasbackend.config

import com.vairuoklaisvai.backend.exception.AppException
import com.vairuoklaisvai.backend.exception.BadRequestException
import com.vairuoklaisvai.backend.exception.ConflictException
import com.vairuoklaisvai.backend.exception.ForbiddenException
import com.vairuoklaisvai.backend.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AppException::class)
    fun handleAppException(e: AppException): ResponseEntity<ProblemDetail> {
        val status =
            when (e) {
                is NotFoundException -> HttpStatus.NOT_FOUND
                is ConflictException -> HttpStatus.CONFLICT
                is ForbiddenException -> HttpStatus.FORBIDDEN
                is BadRequestException -> HttpStatus.BAD_REQUEST
            }
        return ResponseEntity
            .status(status)
            .body(ProblemDetail.forStatusAndDetail(status, e.message))
    }
}
