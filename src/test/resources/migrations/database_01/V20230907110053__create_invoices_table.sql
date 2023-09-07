CREATE TABLE public.invoices
(
    id          serial primary key,
    created_at  timestamp default now(),
    updated_at  timestamp default now(),
    modified_by text      default '',
    json_data   jsonb     default '{}'::jsonb
);

GRANT ALL ON TABLE public.invoices TO test;
