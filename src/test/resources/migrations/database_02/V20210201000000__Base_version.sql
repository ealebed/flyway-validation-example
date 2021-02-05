CREATE TABLE public.scheduled_reports (
    report_id uuid NOT NULL,
    replaced_report_id uuid,
    report_name character varying,
    report_start_time timestamp without time zone,
    report_end_time timestamp without time zone,
    author_user_id integer,
    advertiser_user_id integer,
    advertiser_campaign_id integer,
    fields character varying,
    email_schedule_type character varying,
    email_delivery_day_of_week character varying,
    email_delivery_hour integer,
    email_delivery_minute integer,
    created_on timestamp without time zone DEFAULT now(),
    report_type character varying,
    active boolean,
    deleted boolean,
    time_zone character varying
);

ALTER TABLE public.scheduled_reports OWNER TO test;
