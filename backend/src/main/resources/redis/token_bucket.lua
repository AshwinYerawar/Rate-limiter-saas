-- KEYS[1] = rate limit key
-- ARGV[1] = current timestamp (ms)
-- ARGV[2] = window size (ms)
-- ARGV[3] = limit

local key = KEYS[1]
local now = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])

local window_start = now - window

-- 1️⃣ Remove expired entries
redis.call("ZREMRANGEBYSCORE", key, 0, window_start)

-- 2️⃣ Count current requests
local count = redis.call("ZCARD", key)

-- 3️⃣ Block if limit exceeded
if count >= limit then
    local oldest = redis.call("ZRANGE", key, 0, 0, "WITHSCORES")
    local reset_at = now
    if oldest[2] then
        reset_at = tonumber(oldest[2]) + window
    end
    return {0, limit, 0, reset_at}
end

-- 4️⃣ Add current request
redis.call("ZADD", key, now, now .. "-" .. math.random())

-- 5️⃣ Set TTL (safety)
redis.call("PEXPIRE", key, window)

local remaining = limit - count - 1
local reset_at = now + window

return {1, limit, remaining, reset_at}