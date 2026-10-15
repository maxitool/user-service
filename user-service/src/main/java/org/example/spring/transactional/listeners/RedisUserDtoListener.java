package org.example.spring.transactional.listeners;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.dto.UserDto;
import org.example.spring.enums.SelectOperation;
import org.example.spring.services.RedisUserService;
import org.example.spring.transactional.events.RedisFindAllEvent;
import org.example.spring.transactional.events.RedisFindByAgeEvent;
import org.example.spring.transactional.events.RedisFindByNameEvent;
import org.example.spring.transactional.events.RedisSelectOperationWithUserDtoEvent;
import org.example.spring.transactional.events.RedisUserChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RedisUserDtoListener {
    private final RedisUserService redisUserService;

    private final Map<SelectOperation, Consumer<UserDto>> selectOperationsWithUserDto;

    public RedisUserDtoListener(RedisUserService redisUserService) {
        this.redisUserService = redisUserService;
        if (redisUserService == null) {
            log.error("redisUserService is null");
            this.selectOperationsWithUserDto = Map.of();
            return;
        }
        this.selectOperationsWithUserDto = Map.of(
                SelectOperation.FIND_BY_ID, redisUserService::cacheFindById,
                SelectOperation.FIND_BY_EMAIL, redisUserService::cacheFindByEmail
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSelectOperationWithUserDtoEvent(RedisSelectOperationWithUserDtoEvent event) {
        if (!selectOperationsWithUserDto.containsKey(event.selectOperation())) {
            log.error("Can't find select operation {} inside handleSelectOperationWithUserDtoEvent method, available: {}",
                    event.selectOperation().getValue(),
                    selectOperationsWithUserDto.keySet().stream()
                            .map(item -> item.getValue() + ' ')
                            .collect(Collectors.joining()));
            return;
        }
        selectOperationsWithUserDto.get(event.selectOperation()).accept(event.userDto());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFindAllEvent(RedisFindAllEvent event) {
        redisUserService.cacheFindAll(event.userDtoList());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFindByNameEvent(RedisFindByNameEvent event) {
        redisUserService.cacheFindByName(event.name(), event.userDtoList());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFindByAgeEvent(RedisFindByAgeEvent event) {
        redisUserService.cacheFindByAge(event.age(), event.userDtoList());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserDataChangedEvent(RedisUserChangedEvent event) {
        redisUserService.deleteCachedUserDto(event.userDto());
    }
}
