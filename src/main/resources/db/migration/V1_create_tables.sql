-- public.developers definition

-- Drop table

-- DROP TABLE public.developers;

CREATE TABLE developers (
	id uuid NOT NULL,
	created_at timestamp(6) NULL,
	description varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	updated_at timestamp(6) NULL,
	CONSTRAINT developers_pkey PRIMARY KEY (id),
	CONSTRAINT uk_94dt0wl2vcnni84udppe15h8s UNIQUE (name)
);


-- public.genres definition

-- Drop table

-- DROP TABLE public.genres;

CREATE TABLE genres (
	id uuid NOT NULL,
	created_at timestamp(6) NULL,
	description varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	updated_at timestamp(6) NULL,
	CONSTRAINT genres_pkey PRIMARY KEY (id),
	CONSTRAINT uk_pe1a9woik1k97l87cieguyhh4 UNIQUE (name)
);


-- public.roles definition

-- Drop table

-- DROP TABLE public.roles;

CREATE TABLE roles (
	id uuid NOT NULL,
	created_at timestamp(6) NULL,
	"name" varchar(255) NOT NULL,
	updated_at timestamp(6) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id),
	CONSTRAINT uk_ofx66keruapi6vyqpv6f2or37 UNIQUE (name)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE users (
	id uuid NOT NULL,
	created_at timestamp(6) NULL,
	email varchar(60) NOT NULL,
	"name" varchar(60) NOT NULL,
	"password" varchar(60) NOT NULL,
	updated_at timestamp(6) NULL,
	CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.games definition

-- Drop table

-- DROP TABLE public.games;

CREATE TABLE games (
	id uuid NOT NULL,
	description varchar(255) NOT NULL,
	grade varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	"year" int4 NOT NULL,
	developer_id uuid NOT NULL,
	genre_id uuid NOT NULL,
	CONSTRAINT games_pkey PRIMARY KEY (id),
	CONSTRAINT uk_dp39yy9j9cn10v9vhyr2j1uaa UNIQUE (name),
	CONSTRAINT fkfnb2pp2b4p361k65kaf7kig55 FOREIGN KEY (genre_id) REFERENCES public.genres(id),
	CONSTRAINT fkg4yp4apb1uuwd89u1s1x59s9v FOREIGN KEY (developer_id) REFERENCES public.developers(id)
);


-- public.user_roles definition

-- Drop table

-- DROP TABLE public.user_roles;

CREATE TABLE user_roles (
	user_id uuid NOT NULL,
	role_id uuid NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id),
	CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id)
);