CREATE TABLE public.bundles (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    json_data jsonb DEFAULT '{}'::jsonb,
    modified_by text DEFAULT ''::text
);


ALTER TABLE public.bundles OWNER TO test;
