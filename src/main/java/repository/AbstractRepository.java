package repository;

import annotation.BdTable;
import annotation.MyManyToOne;
import annotation.TableColomn;
import database.H2DatabaseManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> {
    private Class<T> entityClass;

    public AbstractRepository(Class<T> entity) {
        this.entityClass = entity;
    }

    public Integer create(T entity) {
        return save(entity, false);
    }

    public void edit(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        Boolean create = Arrays.asList(fields).stream()
                .filter(f -> "id".equals(getColumnName(f)))
                .anyMatch(f -> {
                    f.setAccessible(true);
                    try {
                        return "id".equals(getColumnName(f)) && !"null".equals(f.get(entity).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return false;
                    }
                });
        save(entity, create);
    }

    public void remove(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        String idStr = Arrays.asList(fields).stream()
                .filter(f -> "id".equals(getColumnName(f)))
                .findFirst()
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        return f.get(entity).toString();
                    } catch (IllegalAccessException e) {
                        return "null";
                    }
                })
                .orElse("null");
        if (!"null".equals(idStr)) {
            try (Connection connection = H2DatabaseManager.getConnection();
                 Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DELETE FROM `fff`.`" + getTableName() + "` where id=" + idStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public T find(Integer id) {
        T out = null;
        try (Connection connection = H2DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM `fff`.`" + getTableName() + "` where id=" + id)) {
            if (!result.next()) {
                throw new RuntimeException("не найден id: " + id);
            }
            out = mapResultSetToEntity(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    protected <E> List<T> findAllBy(E entity) {
        List<T> outs = new ArrayList<>();
        Field[] fields = this.entityClass.getDeclaredFields();
        String colomn = Arrays.asList(fields).stream()
                .filter(f -> f.getType().equals(entity.getClass()))
                .filter(f -> f.isAnnotationPresent(TableColomn.class))
                .findFirst()
                .map(f -> f.getAnnotation(TableColomn.class).name())
                .orElse(null);// todo throw
        String idStr = null;
        try {
            idStr = entity.getClass().getMethod("getId").invoke(entity).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (idStr != null && colomn != null) {
            try (Connection connection = H2DatabaseManager.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet result = statement.executeQuery("SELECT * FROM `fff`.`" + getTableName() + "` WHERE " + colomn + " = " + idStr)) {
                while (result.next()) {
                    outs.add(mapResultSetToEntity(result));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return outs;


    }


    public List<T> findAll() {
        List<T> outs = new ArrayList<>();
        try (Connection connection = H2DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM `fff`.`" + getTableName() + "` ")) {
            while (result.next()) {
                outs.add(mapResultSetToEntity(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outs;
    }

    protected abstract T createEntity();

    protected T mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        T entity = createEntity();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println("dddd" + entity.getClass());
            try {
                if (field.isAnnotationPresent(MyManyToOne.class)) {
                    Object obj = getRepositoryAndInvokeMethod(field, "find", Integer.class, resultSet.getObject(getColumnName(field)));
                    field.set(entity, obj);
                } else {
                    field.set(entity, resultSet.getObject(getColumnName(field)));
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    private Object getRepositoryAndInvokeMethod(Field field, String methodName, Class clazz, Object val) {
        if (field.isAnnotationPresent(MyManyToOne.class)) {
            MyManyToOne manyToOne = field.getAnnotation(MyManyToOne.class);
            Class repoClass = manyToOne.repository();
            Object repoInstance = null;
            Method repoMethod = null;
            Object obj = null;
            try {
                repoInstance = repoClass.newInstance();
                repoMethod = repoClass.getMethod(methodName, clazz);
                obj = repoMethod.invoke(repoInstance, val);
                return obj;
            } catch (IllegalAccessException
                    | NoSuchMethodException
                    | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

        }
        throw new RuntimeException();
    }

    private Integer save(T entity, Boolean flagEdit) {
        Field[] fields = entity.getClass().getDeclaredFields();
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!"id".equals(getColumnName(field)) || flagEdit) {
                    names.add(getColumnName(field));
                    String val = field.get(entity).toString();
                    if ("null".equals(val) && field.isAnnotationPresent(MyManyToOne.class)) {
                        Integer id = (Integer) getRepositoryAndInvokeMethod(field, "create", Object.class, field.get(entity));
                        val = String.valueOf(id);
                    }
                    if ("String".equals(field.getType().getSimpleName())) {
                        val = "'" + val + "'";
                    }
                    values.add(val);
                    map.put(getColumnName(field), val);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        String idEntity = map.get("id");
        String sql;
        if (flagEdit && !"null".equals(idEntity)) {
            String valuesToUpdate = map.entrySet().stream()
                    .filter(entry -> !"id".equals(entry.getKey()))
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(","));
            sql = "UPDATE `fff`.`" + getTableName() + "` SET " + valuesToUpdate + "    WHERE id =" + idEntity;
        } else {
            sql = "INSERT INTO `fff`.`" + getTableName() + "` ( " + names.stream().collect(Collectors.joining(",")) + " ) VALUES (" + values.stream().collect(Collectors.joining(",")) + " )";
        }

        try (Connection connection = H2DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException();
            } else {
                // Получение сгенерированного ключа
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Integer id = generatedKeys.getInt(1);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    private String getTableName() {

        if (entityClass.isAnnotationPresent(BdTable.class)) {
            BdTable bdTableAnnotation = entityClass.getAnnotation(BdTable.class);
            String tableName = bdTableAnnotation.name();
            return tableName;
        } else {
            throw new RuntimeException("для данного  класса таблица не указана");
        }
    }

    private String getColumnName(Field field) {

        if (field.isAnnotationPresent(TableColomn.class)) {
            TableColomn annotation = field.getAnnotation(TableColomn.class);
            return annotation.name();
        } else {
            throw new RuntimeException("для поля не указано имя столбца");
        }
    }


}
