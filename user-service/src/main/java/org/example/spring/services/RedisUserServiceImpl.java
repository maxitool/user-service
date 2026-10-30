package org.example.spring.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.dto.UserDto;
import org.example.spring.enums.SelectOperation;
import org.example.spring.properties.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class RedisUserServiceImpl implements RedisUserService {
    private static final TypeReference<List<UserDto>> LIST_OF_USER_DTO = new TypeReference<>() {
    };
    private static final TypeReference<UserDto> USER_DTO = new TypeReference<>() {
    };
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;
    private Integer ttlDuration;

    public RedisUserServiceImpl(RedisTemplate<String, String> redisTemplate,
                                RedisProperties redisProperties,
                                ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
        this.objectMapper = objectMapper;
    }

    public int getTtlDuration() {
        if (ttlDuration == null) {
            ttlDuration = redisProperties.getTtlDuration();
        }
        return ttlDuration;
    }

    public void cacheFindById(UserDto userDto) {
        cacheWithUserDto(
                SelectOperation.FIND_BY_ID.getValue() + '&' + userDto.id(),
                userDto,
                "FindById");
    }

    public void cacheFindByEmail(UserDto userDto) {
        cacheWithUserDto(
                SelectOperation.FIND_BY_EMAIL.getValue() + '&' + userDto.email(),
                userDto,
                "FindByEmail");
    }

    public void cacheFindAll(List<UserDto> userDtoList) {
        cacheWithUserDtoList(
                SelectOperation.FIND_ALL.getValue(),
                userDtoList,
                "FindAll");
    }

    public void cacheFindByName(String name, List<UserDto> userDtoList) {
        cacheWithUserDtoList(
                SelectOperation.FIND_BY_NAME.getValue() + '&' + name,
                userDtoList,
                "FindByName");
    }

    public void cacheFindByAge(Integer age, List<UserDto> userDtoList) {
        cacheWithUserDtoList(
                SelectOperation.FIND_BY_AGE.getValue() + '&' + age,
                userDtoList,
                "FindByAge");
    }

    public UserDto getCachedFindById(Long id) {
        return getCachedUserDto(
                SelectOperation.FIND_BY_ID.getValue() + '&' + id,
                "FindById");
    }

    public UserDto getCachedFindByEmail(String email) {
        return getCachedUserDto(
                SelectOperation.FIND_BY_EMAIL.getValue() + '&' + email,
                "FindByEmail");
    }

    public List<UserDto> getCachedFindAll() {
        return getCachedUserDtoList(
                SelectOperation.FIND_ALL.getValue(),
                "FindAll");
    }

    public List<UserDto> getCachedFindByName(String name) {
        return getCachedUserDtoList(
                SelectOperation.FIND_BY_NAME.getValue() + '&' + name,
                "FindByName");
    }

    public List<UserDto> getCachedFindByAge(Integer age) {
        return getCachedUserDtoList(
                SelectOperation.FIND_BY_AGE.getValue() + '&' + age,
                "FindByAge");
    }

    public void deleteCachedFindAll() {
        deleteCached(SelectOperation.FIND_ALL.getValue());
    }

    public void deleteCachedFindByName(String name) {
        deleteCached(SelectOperation.FIND_BY_NAME.getValue() + '&' + name);
    }

    public void deleteCachedFindByAge(Integer age) {
        deleteCached(SelectOperation.FIND_BY_AGE.getValue() + '&' + age);
    }

    public void deleteCachedUserDto(UserDto userDto) {
        deleteCached(SelectOperation.FIND_BY_ID.getValue() + '&' + userDto.id());
        deleteCached(SelectOperation.FIND_BY_EMAIL.getValue() + '&' + userDto.email());
        deleteCachedFindAll();
        deleteCachedFindByName(userDto.name());
        deleteCachedFindByAge(userDto.age());
    }

    protected void deleteCached(String key) {
        redisTemplate.delete(key);
        log.debug("Cached key {} has been deleted", key);
    }

    protected void cacheWithUserDto(String key, UserDto userDto, String methodName) {
        try {
            String json = objectMapper.writeValueAsString(userDto);
            redisTemplate.opsForValue().set(
                    key,
                    json,
                    Duration.ofMinutes(getTtlDuration()));
            log.info("{} operation has been cached key: {}, value: {}", methodName, key, json);
        } catch (JsonProcessingException ex) {
            log.error("Can't serialize UserDto to JSON, exception: {}", ex.getMessage());
        }
    }

    protected void cacheWithUserDtoList(String key, List<UserDto> userDtoList, String methodName) {
        try {
            String json = objectMapper.writeValueAsString(userDtoList);
            redisTemplate.opsForValue().set(
                    key,
                    json,
                    Duration.ofMinutes(getTtlDuration()));
            log.info("{} operation has been cached key: {}, values: {}",
                    methodName,
                    key,
                    json);
        } catch (JsonProcessingException ex) {
            log.error("Can't serialize List<UserDto> to JSON, exception: {}", ex.getMessage());
        }
    }

    protected UserDto getCachedUserDto(String key, String methodName) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.debug("Didn't find {} operation key: {}", methodName, key);
            return null;
        }
        try {
            UserDto userDto = objectMapper.readValue(value, USER_DTO);
            log.debug("{} operation has been got key : {}, value:\n{}", methodName, key, value);
            return userDto;
        } catch (Exception ex) {
            log.error("Can't serialize JSON to UserDto , exception: {}", ex.getMessage());
        }
        return null;
    }

    protected List<UserDto> getCachedUserDtoList(String key, String methodName) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.debug("Didn't find {} operation key: {}", methodName, key);
            return List.of();
        }
        try {
            List<UserDto> userDtoList = objectMapper.readValue(value, LIST_OF_USER_DTO);
            log.debug("{} operation has been got key : {}, value:\n{}", methodName, key, value);
            return userDtoList;
        } catch (Exception ex) {
            log.error("Can't serialize JSON to List<UserDto> , exception: {}", ex.getMessage());
        }
        return List.of();
    }
}
