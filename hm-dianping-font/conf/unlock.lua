-- 获取锁中的线程标识
local id = redis.call('get',KEYS[1])
-- 比较线程标识与锁中的标识是否一致
if(id == ARGV[1]) then
    -- 一致释放锁
    redis.call('del',KEYS[1])
end
-- 不一致 返回0
return 0