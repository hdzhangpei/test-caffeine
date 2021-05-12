# test-caffeine
参考：https://www.cnblogs.com/fnlingnzb-learner/p/11025565.html

    /**
     * 默认key生成：
     * 默认key的生成按照以下规则：
     * - 如果没有参数,则使用0作为key
     * - 如果只有一个参数，使用该参数作为key
     * - 如果又多个参数，使用包含所有参数的hashCode作为key
     *
     *
     * 其中checkWarehouse，includeUsed并不适合当做缓存的key.针对这种情况，Cacheable 允许指定生成key的关键属性，并且支持支持SpringEL表达式。（推荐方法）
     * @Cacheable(value="container2", key="#isbn")
     * public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
     * @Cacheable(value="container2", key="#isbn.rawNumber")
     * public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
     * @Cacheable(value="container2", key="T(someType).hash(#isbn)")
     * public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
     * @Cacheable(value="container2", key="#map['bookid'].toString()")
     * public Book findBook(Map<String, Object> map)
     *
     * 缓存的同步 sync：
     * 在多线程环境下，某些操作可能使用相同参数同步调用。默认情况下，缓存不锁定任何资源，可能导致多次计算，而违反了缓存的目的。对于这些特定的情况，属性 sync 可以指示底层将缓存锁住，使只有一个线程可以进入计算，而其他线程堵塞，直到返回结果更新到缓存中。
     * 例：
     * @Cacheable(value="container2", sync="true")
     *
     * 根据满足条件的入参判断是否使用缓存
     * 属性condition：
     * 有时候，一个方法可能不适合一直缓存（例如：可能依赖于给定的参数）。属性condition支持这种功能，通过SpEL 表达式来指定可求值的boolean值，为true才会缓存（在方法执行之前进行评估）。
     * 例：
     * @Cacheable(value="container2", condition="#name.length < 32")
     *
     * 根据返回结果判断是否存入缓存
     * 此外，还有一个unless 属性可以用来是决定是否添加到缓存。与condition不同的是，unless表达式是在方法调用之后进行评估的。如果返回false，才放入缓存（与condition相反）。 #result指返回值 例：
     * @Cacheable(cacheNames="book", condition="#name.length < 32", unless="#result.name.length > 5"")
     */
     
     /**
     * @CachePut
     * 如果缓存需要更新，且不干扰方法的执行,可以使用注解@CachePut。@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
     * @CachePut(cacheNames="book", key="#isbn")
     * public Book updateBook(ISBN isbn, BookDescriptor descriptor)
     * 注意：应该避免@CachePut 和 @Cacheable同时使用的情况。
     *
     * @CacheEvict
     * spring cache不仅支持将数据缓存，还支持将缓存数据删除。此过程经常用于从缓存中清除过期或未使用的数据。
     * @CacheEvict要求指定一个或多个缓存，使之都受影响。此外，还提供了一个额外的参数allEntries 。表示是否需要清除缓存中的所有元素。默认为false，表示不需要。当指定了allEntries为true时，Spring Cache将忽略指定的key。有的时候我们需要Cache一下清除所有的元素。
     * @CacheEvict(cacheNames="books", allEntries=true)
     * public void loadBooks(InputStream batch)
     *
     * 清除操作默认是在对应方法成功执行之后触发的，即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。使用beforeInvocation可以改变触发清除操作的时间，当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素。
     * @CacheEvict(cacheNames="books", beforeInvocation=true)
     * public void loadBooks(InputStream batch)
     *
     * @CacheConfig
     * 有时候一个类中可能会有多个缓存操作，而这些缓存操作可能是重复的。这个时候可以使用@CacheConfig
     * @CacheConfig("books")
     * public class BookRepositoryImpl implements BookRepository {
     * @Cacheable
     * public Book findBook(ISBN isbn) {...}
     * }
     *
     * keyGenerator
     * https://blog.csdn.net/qq_42420773/article/details/103870662
     *
     *     @Cacheable(cacheNames = "ids_cache",keyGenerator = "myKeyGenerator")
     *     public List<String>
     *     selectList(Map<String, Object> map) {
     *         List<String> list = sysLoginRegisterLogMapper.selectListByMap(map);
     *         return list;
     *     }
     *
     *     自定义策略
     *     @Component
     *     public class MyKeyGenerator implements KeyGenerator {
     *
     *     @Override
     *     public Object generate(Object target, Method method, Object... params) {
     *         if (params.length == 0) {
     *             return SimpleKey.EMPTY;
     *         }
     *         Object param = params[0];
     *         // 参数为map自定义key=类名+方法名+map的key-value值
     *         if (param instanceof Map) {
     *             StringBuilder builder = new StringBuilder();
     *             // 分隔符
     *             String sp = ".";
     *             builder.append(target.getClass().getSimpleName()).append(sp);
     *             builder.append(method.getName()).append(sp);
     *             Map<String, Object> map = (Map<String, Object>) param;
     *             if (map.isEmpty()) {
     *                 return builder.toString();
     *             }
     *             for (String key : map.keySet()) {
     *                 builder.append(key).append("-").append(map.get(key)).append(sp);
     *             }
     *             return builder.toString();
     *         }
     *         return new SimpleKey(params);
     *     }
     * }
     *
     *
     * CacheResolver 与 CacheManager 的关系有点类似于 KeyGenerator 跟 key。spring 默认提供了一个 SimpleCacheResolver，开发者可以自定义并通过 @Bean 来注入自定义的解析器，以实现更灵活的检索。
     */
     
