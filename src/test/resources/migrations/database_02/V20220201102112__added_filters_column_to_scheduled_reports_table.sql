ALTER TABLE scheduled_reports
    ADD COLUMN IF NOT EXISTS filters VARCHAR;
