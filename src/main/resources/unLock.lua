-- 比较线程标识与锁一致
if (redis.call('get',KEYS[1]) == ARGV[1]) then
    return redis.call('del',KEYS[1])
end