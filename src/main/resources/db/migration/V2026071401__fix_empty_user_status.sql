update users
set status = 'ACTIVE'
where status is null or trim(status) = '';
