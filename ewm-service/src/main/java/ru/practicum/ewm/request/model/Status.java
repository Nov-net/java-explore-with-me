package ru.practicum.ewm.request.model;

public enum Status {
    PENDING, // ожидает подтверждение
    APPROVED, // подтверждено
    REJECTED, // отклонено
    CANCELED // отменено создателем

}
