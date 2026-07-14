update users
set status = 'ACTIVE'
where status not in ('ACTIVE', 'SUSPENDED');
