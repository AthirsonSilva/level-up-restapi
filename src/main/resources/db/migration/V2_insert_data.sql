INSERT INTO public.developers (id,created_at,description,name,updated_at) VALUES
	 ('eb85d418-24ff-4fa4-b254-6d7727fcccb3','2023-06-03 19:25:20.509386','Game without developer','No Developer','2023-06-03 19:25:20.50943'),
	 ('71435c40-5da3-4b97-9bd8-be97c0475429','2023-06-03 19:25:20.524376','Eligendi et dolores.','Hoeger Inc','2023-06-03 19:25:20.524421'),
	 ('b55ef0c3-9bc5-40da-bf64-08532404ec0d','2023-06-03 19:25:20.530447','Error esse temporibus cupiditate fugit ipsum.','Kris Inc','2023-06-03 19:25:20.530476'),
	 ('2e26e752-576c-417d-a581-aae9cc04f992','2023-06-03 19:25:20.536854','Ipsa est velit accusantium.','Jerde, Rolfson and Marks','2023-06-03 19:25:20.536884'),
	 ('7e327abd-2a2f-4a99-a3be-ad9903d6d619','2023-06-03 19:25:20.543481','Sed sed qui repellat.','Goyette, Schulist and Waters','2023-06-03 19:25:20.54351');

INSERT INTO public.games (id,description,grade,name,"year",developer_id,genre_id) VALUES
	 ('9ceec51d-dd81-4754-97ac-fbb81860f42a','Id cum iure.','AA','Paucek LLC',0,'eb85d418-24ff-4fa4-b254-6d7727fcccb3','cc3ee476-24fc-4853-952e-17592e0f2e1d'),
	 ('ff838be0-af3d-4f68-a1a2-0cb102ebdfc4','Dolorem et consectetur numquam doloribus.','A','Langosh and Sons',0,'eb85d418-24ff-4fa4-b254-6d7727fcccb3','cc3ee476-24fc-4853-952e-17592e0f2e1d'),
	 ('5b37ead0-bada-48e0-9f42-5949af7f1cc0','Veritatis quia omnis perspiciatis deleniti.','AAA','Jacobson LLC',0,'eb85d418-24ff-4fa4-b254-6d7727fcccb3','cc3ee476-24fc-4853-952e-17592e0f2e1d'),
	 ('77683e88-d017-4322-a53d-63317148942a','Sequi officiis repudiandae beatae rerum aut dolor et.','A','Kerluke, Nolan and Lehner',0,'eb85d418-24ff-4fa4-b254-6d7727fcccb3','cc3ee476-24fc-4853-952e-17592e0f2e1d'),
	 ('6c41fcaf-d83c-417f-9ab8-7751dd27dc5c','Sit id iste eos et quia.','AAA','Rogahn and Sons',0,'eb85d418-24ff-4fa4-b254-6d7727fcccb3','cc3ee476-24fc-4853-952e-17592e0f2e1d');

INSERT INTO public.genres (id,created_at,description,name,updated_at) VALUES
	 ('cc3ee476-24fc-4853-952e-17592e0f2e1d','2023-06-03 19:25:20.458314','Games without genre','No Genre','2023-06-03 19:25:20.45834'),
	 ('b155bc97-66ae-4739-8ef2-f116d14f21ed','2023-06-03 19:25:20.476279','Corporis occaecati quas rerum.','Fantasy','2023-06-03 19:25:20.476306'),
	 ('6b6e691f-27fc-41d0-8de8-35b85af8ea06','2023-06-03 19:25:20.488564','Assumenda eum aliquam eaque at voluptatem repudiandae dicta.','Short story','2023-06-03 19:25:20.488595'),
	 ('30334758-faaf-4f66-93c3-2dc36f7e5371','2023-06-03 19:25:20.494558','Animi est in soluta similique omnis.','Comic/Graphic Novel','2023-06-03 19:25:20.494593'),
	 ('16cca1c1-d56f-497e-b687-6f0eb0e845f0','2023-06-03 19:25:20.500087','Ut molestiae fugit numquam nihil.','Suspense/Thriller','2023-06-03 19:25:20.500115');

INSERT INTO public.roles (id,created_at,name,updated_at) VALUES
	 ('9dbac7e3-f3e1-4b9a-827a-65437285d812','2023-06-03 19:25:19.635936','ADMIN','2023-06-03 19:25:19.636023'),
	 ('c736414b-427a-4565-aaaa-59dbb492eb19','2023-06-03 19:25:19.652418','USER','2023-06-03 19:25:19.65246');

INSERT INTO public.user_roles (user_id,role_id) VALUES
	 ('bb9e488b-0a7b-4d0b-8185-cd12b4949361','9dbac7e3-f3e1-4b9a-827a-65437285d812'),
	 ('672d8ad6-7769-425e-ad89-b50a4b46b557','c736414b-427a-4565-aaaa-59dbb492eb19'),
	 ('84c76505-7d07-468d-9f6a-3b506385ed28','c736414b-427a-4565-aaaa-59dbb492eb19'),
	 ('be1345ee-0444-484a-863e-c5f72d8630de','c736414b-427a-4565-aaaa-59dbb492eb19'),
	 ('1f79b203-0ed6-43ca-b847-b3087d5c0270','c736414b-427a-4565-aaaa-59dbb492eb19'),
	 ('9dda6e25-ee0d-458a-a598-5934042fd263','c736414b-427a-4565-aaaa-59dbb492eb19');

INSERT INTO public.users (id,created_at,email,name,"password",updated_at) VALUES
	 ('bb9e488b-0a7b-4d0b-8185-cd12b4949361','2023-06-03 19:25:19.798271','admin@admin.com','admin','$2a$10$K9no9asHKVIAJBvdxQ20Q.oIWJku9dGGR.9yxPq0UWug.V7UEM26.','2023-06-03 19:25:19.798344'),
	 ('672d8ad6-7769-425e-ad89-b50a4b46b557','2023-06-03 19:25:19.915509','user@user.com','user','$2a$10$w1/iW3ms5IR1Os44Gw/i6u73s1ty5yeIzhTk5UVH9XxVdu7qZ3M/.','2023-06-03 19:25:19.915539'),
	 ('84c76505-7d07-468d-9f6a-3b506385ed28','2023-06-03 19:25:20.096407','lois.osinski@gmail.com','Mr. Merle Feeney','$2a$10$b/5IfEGhabLhHOaSp5IF2OXq1U41TucayT0EUmy34/4w18pdcdIUa','2023-06-03 19:25:20.096438'),
	 ('be1345ee-0444-484a-863e-c5f72d8630de','2023-06-03 19:25:20.214147','patrica.berge@yahoo.com','August Russel DVM','$2a$10$BqdueIkAgDyhVek6gHVlXOTtvSuc4jnboyvLdo.D8T.ESxscWveuK','2023-06-03 19:25:20.214176'),
	 ('1f79b203-0ed6-43ca-b847-b3087d5c0270','2023-06-03 19:25:20.334318','lorretta.hackett@hotmail.com','Timmy Koss','$2a$10$8D11oDY4Lf4ppAPbNJYeIeMOuGdGB56Lkk9VKWKxMyawJ4LkeEPFC','2023-06-03 19:25:20.334342'),
	 ('9dda6e25-ee0d-458a-a598-5934042fd263','2023-06-03 19:25:20.446017','melvin.borer@gmail.com','Taylor Morissette','$2a$10$C1C./3BcaYzcFWQA3i4MiuyMrL2D0FBdtizOGd3Yg3u5OB6hcfosS','2023-06-03 19:25:20.446045');