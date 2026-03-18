-- Очистка таблиц перед тестом (в правильном порядке из-за FK)
delete from user_likes;
delete from recipe_ingredients;
delete from recipes;
delete from ingredients;

-- Сброс автоинкремента (для PostgreSQL)
alter sequence ingredients_id_seq RESTART with 1;
alter sequence recipes_id_seq RESTART with 1;
alter sequence recipe_ingredients_id_seq RESTART with 1;
alter sequence user_likes_id_seq RESTART with 1;

-- Ингредиенты
insert into ingredients (name, created_at) values
    ('курица', current_timestamp),
    ('яблоки', CURRENT_TIMESTAMP),
    ('сыр', CURRENT_TIMESTAMP),
    ('лук', CURRENT_TIMESTAMP),
    ('рис', CURRENT_TIMESTAMP);

-- Рецепты
insert into recipes (title, description, instructions, likes_count, version, created_at, updated_at) values
    ('Курица с яблоками', 'Запеченная курица с яблоками', '1. Нарезать. 2. Запечь.', 10, 0, current_timestamp, current_timestamp),
    ('Куриный суп', 'Суп с курицей и овощами', '1. Сварить бульон. 2. Добавить овощи.', 5, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Яблочный пирог', 'Пирог со свежими яблоками', '1. Замесить тесто. 2. Запечь.', 8, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Связи рецепт-ингредиент
-- Рецепт 1: курица + яблоки + сыр
insert into recipe_ingredients (recipe_id, ingredient_id, quantity, created_at) values
    (1, 1, '500 г', current_timestamp),  -- курица
    (1, 2, '2 шт', CURRENT_TIMESTAMP),   -- яблоки
    (1, 3, '100 г', CURRENT_TIMESTAMP);  -- сыр

-- Рецепт 2: курица + лук + рис
insert into recipe_ingredients (recipe_id, ingredient_id, quantity, created_at) values
    (2, 1, '300 г', current_timestamp),  -- курица
    (2, 4, '1 шт', CURRENT_TIMESTAMP),   -- лук
    (2, 5, '200 г', CURRENT_TIMESTAMP);  -- рис

-- Рецепт 3: яблоки + сыр
insert into recipe_ingredients (recipe_id, ingredient_id, quantity, created_at) values
    (3, 2, '3 шт', current_timestamp),   -- яблоки
    (3, 3, '150 г', CURRENT_TIMESTAMP);  -- сыр