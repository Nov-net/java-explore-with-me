package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

import static ru.practicum.ewm.compilation.dto.CompilationMapper.*;
import static ru.practicum.ewm.validator.Validator.isValidCompilation;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    /*POST /admin/compilations - создание новой подборки*/
    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        log.info("Получена новая подборка {}", compilationDto);
        Compilation compilation = toCompilation(compilationDto);

        log.info("В подборке {} events: {}", compilationDto.getEvents().size(), compilationDto.getEvents());
        compilation.setEvents(eventRepository.findAllByIdIn(compilationDto.getEvents()));

        compilation = repository.save(compilation);
        log.info("Сохранена новая подборка {}", compilation);

        return toCompilationDto(compilation);
    }

    /*PATCH /admin/compilations/{compId} - обновление подборки*/
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto compilationDto) {
        log.info("Запрос на обновление подборки с id {}: {}", compId, compilationDto);

        Compilation compilation = isValidCompilation(compId, repository.findById(compId));
        log.info("Текущая подборка проверена по id: {}", compilation);

        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findAllByIdIn(compilationDto.getEvents()));
            log.info("Подборке установлено {} events: {}", compilationDto.getEvents().size(), compilation.getEvents());
        }

        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }

        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }

        Compilation updCompilation = repository.save(compilation);
        log.info("Подборка обновлена: {}", updCompilation);
        return toCompilationDto(updCompilation);
    }

    /*DELETE /admin/categories/{catId} - удаление категории по catId*/
    @Override
    public boolean deleteCompilation(Long compId) {
        log.info("Запрос на удаление подборки с id {}", compId);
        repository.delete(isValidCompilation(compId, repository.findById(compId)));
        log.info("Подборка с id {} удалена", compId);
        return true;
    }

    /*GET /admin/categories - получение списка категорий*/
    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size) {
        log.info("Запрос на получение списка подборок pinned {}, from {}, size {}", pinned, from, size);

        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return mapToCompilationDto(repository.findAllByPinned(pinned, pageable));
        }

        return mapToCompilationDto(repository.findAll(pageable).toList());
    }

    /*GET /admin/categories/{catId} - получение категории по id*/
    public CompilationDto getCompilationById(Long compId) {
        log.info("Получение подборки по id {}", compId);
        return toCompilationDto(isValidCompilation(compId, repository.findById(compId)));
    }

}