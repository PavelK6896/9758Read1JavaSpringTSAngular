insert into client.users (created, email, enabled, password, username)
select '2021-01-30 10:25:57.953057',
       '@yandex.ru',
       true,
       '{bcrypt}$2a$10$0q0yPhhE.GzzzMl089vHKujpqJlGxly3dm2VaIBv0xhDdhKbuwjby',
       'Pavel'
WHERE NOT EXISTS(
        SELECT id FROM client.users WHERE id = 1
    );

insert into client.users (created, email, enabled, password, username)
select '2021-01-30 10:25:57.953057',
       '@yandex.ru2',
       true,
       '{bcrypt}$2a$10$0q0yPhhE.GzzzMl089vHKujpqJlGxly3dm2VaIBv0xhDdhKbuwjby',
       'Pavel2'
WHERE NOT EXISTS(
        SELECT id FROM client.users WHERE id = 2
    );

insert into client.token (token, user_id)
select '92fd5023-9950-4d1c-a6ae-8c921cb0b262', 1
WHERE NOT EXISTS(
        SELECT id FROM client.token WHERE id = 1
    );

insert into post.subreddit (description, name, user_id)
select 'Computer equipment.', 'Technical', 1
WHERE NOT EXISTS(
        SELECT id FROM post.subreddit WHERE id = 1
    );

insert into post.subreddit (description, name, user_id)
select 'A programming language is a formal language comprising a set of instructions that produce various kinds of output.',
       'Programming language',
       1
WHERE NOT EXISTS(
        SELECT id FROM post.subreddit WHERE id = 2
    );

insert into post.post (created_date, description, post_name, vote_count, subreddit_id, user_id)
select '2021-01-30 11:02:42.572943',
       '<br/><h2 class="ql-indent-1"><span style="background-color: rgb(204, 232, 204);">Площадь чипа — 462 квадратных сантиметра.</span></h2>',
       'Самый большой процессор в мире — Cerebras CS-1.',
       null,
       1,
       1
WHERE NOT EXISTS(
        SELECT post_id FROM post.post WHERE post_id = 1
    );

insert into post.post (created_date, description, post_name, vote_count, subreddit_id, user_id)
select '2021-01-30 11:03:32.656813',
       '<br/><p><strong style="color: rgb(102, 163, 224);"><em>Технология Seagate MACH.2 удваивает производительность традиционных жестких дисков.</em></strong></p><p><br></p>',
       'Двойной привод hdd - Seagate MACH.2.',
       null,
       1,
       1
WHERE NOT EXISTS(
        SELECT post_id FROM post.post WHERE post_id = 2
    );

insert into post.post (created_date, description, post_name, vote_count, subreddit_id, user_id)
select '2021-01-30 11:06:47.074138',
       '<br/><p class="ql-indent-1"><span style="background-color: rgb(255, 255, 204);">Записать функцию в переменную.</span></p><pre class="ql-syntax ql-indent-2" spellcheck="false">val parse: (String) -&gt; List&lt;Int&gt; = { it.split(":").map(String::toInt) }</pre><p><br></p>',
       'Функция в Kotlin.',
       null,
       2,
       1
WHERE NOT EXISTS(
        SELECT post_id FROM post.post WHERE post_id = 3
    );

insert into post.comment (created_date, text, post_id, user_id)
select '2021-01-30 11:07:58.436678', 'well', 2, 1
WHERE NOT EXISTS(
        SELECT id FROM post.comment WHERE id = 1
    );

insert into post.comment (created_date, text, post_id, user_id)
select '2021-01-30 11:08:56.346776', 'cool', 1, 1
WHERE NOT EXISTS(
        SELECT id FROM post.comment WHERE id = 2
    );

insert into post.vote (vote_type, post_id, user_id)
select 0, 1, 1
WHERE NOT EXISTS(
        SELECT vote_id FROM post.vote WHERE vote_id = 1
    );

insert into post.vote (vote_type, post_id, user_id)
select 0, 2, 1
WHERE NOT EXISTS(
        SELECT vote_id FROM post.vote WHERE vote_id = 2
    );

