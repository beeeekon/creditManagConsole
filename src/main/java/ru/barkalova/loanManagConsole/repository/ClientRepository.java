package ru.barkalova.loanManagConsole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.barkalova.loanManagConsole.model.entity.Client;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /*
    * Как работает:
    * <название операции, которую представляет JpaRepository><поле в entity><ключевые слова>
    * операции:
    CRUD операции
    <S extends T> S save(S entity);           // Сохранить/обновить
    Optional<T> findById(ID id);              // Найти по ID
    boolean existsById(ID id);                // Проверить существование
    long count();                             // Количество записей
    void deleteById(ID id);                   // Удалить по ID
    void delete(T entity);                    // Удалить сущность
    void deleteAll();                         // Удалить все

    // Поиск
    List<T> findAll();                        // Найти все
    List<T> findAllById(Iterable<ID> ids);    // Найти по списку ID

    // Пагинация и сортировка
    Page<T> findAll(Pageable pageable);       // Постраничный вывод
    List<T> findAll(Sort sort);               // Сортировка

    // Другие методы
    void flush();                             // Принудительная синхронизация с БД
    T getOne(ID id);                          // Получить ссылку (без загрузки)
    *
    *
    *
    * - Containing = LIKE %значение%
    * - IgnoreCase = LOWER() для регистронезависимости
    * */


    Optional<Client> findByPassportNumber(String passportNumber);

    boolean existsByEmail(String email);

    boolean existsByPassportNumber(String passportNumber);

    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    List<Client> findByPassportNumberContainingIgnoreCase(String passportNumber);

    List<Client> findByStatus(ClientStatus status);

    List<Client> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    long countByStatus(ClientStatus status);

    Optional<Client> findByEmail(String email);
}
