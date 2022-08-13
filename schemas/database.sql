drop database if exists FireEmblemMulti;
create database FireEmblemMulti;
use FireEmblemMulti;

create table players (
    player_id int primary key not null auto_increment,
    username varchar(64) not null,
    password varchar(256) not null,
    email varchar(128) unique not null,
    game_token varchar(256) null,
    current_preset int default 0 not null
);

create table player_roles (
    player_id int not null,
    role varchar(64) not null,
    constraint role_to_player foreign key(player_id) references players(player_id)
        on delete CASCADE
        on update CASCADE
);

create table boards (
  board_id int primary key not null auto_increment,
  player_a_id int not null,
  player_b_id int not null,
  current_player_id int not null,
  width int not null default(10),
  height int not null default(10),
  constraint player_a_to_player foreign key(player_a_id) references players(player_id)
          on delete RESTRICT
          on update CASCADE,
  constraint player_b_to_player foreign key(player_b_id) references players(player_id)
          on delete RESTRICT
          on update CASCADE,
  constraint current_player_to_player foreign key(current_player_id) references players(player_id)
          on delete RESTRICT
          on update CASCADE
);

create table player_character_presets (
    preset_id int not null primary key auto_increment,
    player_id int not null,
    constraint preset_to_player foreign key(player_id) references players(player_id)
        on delete CASCADE
        on update CASCADE
);

create table game_characters (
    game_character_id int not null primary key auto_increment,
    preset_id int not null,
    name varchar(64) not null,
    remaining_hp int not null,
    current_equipped_item int not null default 0,
    character_class enum(
                'PALADIN', 'GREAT_KNIGHT', 'GENERAL', 'SWORDMASTER', 'HERO', 'WARRIOR', 'SNIPER',
                'TRICKSTER', 'FALCON_KNIGHT', 'GRIFFON_RIDER', 'SAGE', 'DARK_KNIGHT', 'VALKYRIE'
    ) not null,
    moved boolean not null default false,
    constraint game_character_to_preset foreign key(preset_id) references player_character_presets(preset_id)
        on delete CASCADE
        on update CASCADE
);

create table character_stats (
    game_character_id int not null,
    stat enum('HEALTH', 'STRENGTH', 'DEFENSE', 'MAGICK', 'RESISTANCE', 'SKILL', 'LUCK', 'SPEED') not null,
    `value` int not null,
    constraint stat_to_game_character foreign key(game_character_id) references game_characters(game_character_id)
        on delete CASCADE
        on update CASCADE
);

create table character_items (
    game_character_id int not null,
    name varchar(64) not null,
    mt int not null,
    hit_percent int not null,
    critical_percent int not null,
    `range` int not null,
    attack_category enum('PHYSICAL', 'MAGICAL', 'NONE') not null,
    weapon_category enum('SWORD', 'LANCE', 'BOW', 'AXE', 'TOME', 'STAFF') not null,
    weight int not null,
    constraint item_to_game_character foreign key(game_character_id) references game_characters(game_character_id)
        on delete CASCADE
        on update CASCADE
);

create table spots (
    spot_id int not null primary key auto_increment,
    board_id int not null,
    x int not null,
    y int not null,
    terrain enum('GRASS', 'PLAIN', 'FORREST', 'MOUNTAIN', 'FORTRESS', 'SAND') not null,
    constraint spot_to_board foreign key(board_id) references boards(board_id)
        on delete CASCADE
        on update CASCADE
);

create table character_pairs (
    pair_id int not null primary key auto_increment,
    lead_character_id int not null,
    support_character_id int default null,
    spot_id int null,
    constraint lead_character_to_game_character foreign key(lead_character_id) references game_characters(game_character_id)
        on delete RESTRICT
        on update CASCADE,
    constraint support_character_to_game_character foreign key(support_character_id) references game_characters(game_character_id)
        on delete RESTRICT
        on update CASCADE,
    constraint pair_to_spot foreign key(spot_id) references spots(spot_id)
        on delete CASCADE
        on update CASCADE
);

insert into players values (1,'marek', '$2a$12$G//VXe476Iw53pjiWCOeTeYCbGUMGmOxu4HykQ7Q1ndjOzke5NMtu','marekseget@onet.pl',null, 0);
insert into player_roles values (1,'ADMIN');

