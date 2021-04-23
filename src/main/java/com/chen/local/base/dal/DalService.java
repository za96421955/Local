//package com.chen.local.dal;
//
//import com.suning.framework.dal.client.DalClient;
//import com.suning.framework.dal.client.support.Configuration;
//import com.suning.framework.dal.client.support.MappedStatement;
//import com.suning.framework.dal.client.support.ShardingDalClient;
//import com.suning.framework.dal.client.support.executor.MappedSqlExecutor;
//import com.suning.framework.dal.client.support.rowmapper.RowMapperFactory;
//import com.suning.framework.dal.util.DalUtils;
//import com.suning.framework.sedis.ReflectionUtils;
//import com.suning.logistics.jwms.dal.sql.Column;
//import com.suning.logistics.jwms.dal.sql.SortColumn;
//import com.suning.logistics.jwms.dal.sql.SortObject;
//import com.suning.logistics.jwms.exception.RuntimeException;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @version v1.0
// * @Description: DAL处理服务
// * <p>〈功能详细描述〉</p>
// *
// * @ClassName DalHandlerImpl
// * @author 陈晨
// * @date 2019/11/17 12:50
// */
//@Service("dalService")
//public class DalService {
//    private Logger logger = LoggerFactory.getLogger(DalService.class);
//
//    public Configuration getConfiguration(DalClient dalClient) {
//        try {
//            logger.debug("{} read get configuration", dalClient.getClass());
//            Field field = ReflectionUtils.findField(dalClient.getClass(), null, Configuration.class);
//            field.setAccessible(true);
//            return (Configuration) field.get(dalClient);
//        } catch (Exception e) {
//            logger.error("configuration not found, {}", e.getMessage(), e);
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    public MappedSqlExecutor getMappedSqlExecutor(DalClient dalClient
//            , String sqlId, Map<String, Object> paramMap) {
//        try {
//            logger.debug("{} read get mappedSqlExecutor", dalClient.getClass());
//            // ShardingDalClient
//            if (dalClient instanceof ShardingDalClient) {
//                Method method = ReflectionUtils.findMethod(dalClient.getClass(),
//                        "lookupMappedSqlExecutor", String.class, Object.class);
//                method.setAccessible(true);
//                List<MappedSqlExecutor> mappedSqlExecutors = (List<MappedSqlExecutor>) method.invoke(dalClient, sqlId, paramMap);
//                if (mappedSqlExecutors.size() != 1) {
//                    method = ReflectionUtils.findMethod(dalClient.getClass(),
//                            "throwNotSupportMultShardException", String.class);
//                    method.setAccessible(true);
//                    method.invoke(dalClient, sqlId);
//                }
//                return mappedSqlExecutors.get(0);
//            }
//            // DalClient
//            Field field = ReflectionUtils.findField(dalClient.getClass(), null, MappedSqlExecutor.class);
//            field.setAccessible(true);
//            return (MappedSqlExecutor)field.get(dalClient);
//        } catch (Exception e) {
//            logger.error("sqlId={} mappedSqlExecutor not found"
//                            + ", or shardRouter not found(please check sharding configuration)"
//                            + ", exception={}"
//                    , sqlId, e.getMessage(), e);
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    public String getSQL(Configuration configuration, MappedSqlExecutor mappedSqlExecutor
//            , String sqlId, Map<String, Object> paramMap) {
//        if (configuration == null || mappedSqlExecutor == null) {
//            return null;
//        }
//        MappedStatement stmt = configuration.getMappedStatement(sqlId, true);
//        Method method = ReflectionUtils.findMethod(mappedSqlExecutor.getClass(),
//                "applyStatementSettings", MappedStatement.class);
//        method.setAccessible(true);
//        try {
//            method.invoke(mappedSqlExecutor, stmt);
//            return stmt.getBoundSql(paramMap);
//        } catch (Exception e) {
//            logger.error("sqlId={} get fail, {}", sqlId, e.getMessage(), e);
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    public String getSQL(DalClient dalClient, String sqlId, Map<String, Object> paramMap) {
//        Configuration configuration = this.getConfiguration(dalClient);
//        MappedSqlExecutor mappedSqlExecutor = this.getMappedSqlExecutor(dalClient, sqlId, paramMap);
//        return this.getSQL(configuration, mappedSqlExecutor, sqlId, paramMap);
//    }
//
//    public String getSQLSource(Configuration configuration, MappedSqlExecutor mappedSqlExecutor
//            , String sqlId, Map<String, Object> paramMap) {
//        try {
//            MappedStatement stmt = configuration.getMappedStatement(sqlId, true);
//            return stmt.getSqlSource();
//        } catch (Exception e) {
//            logger.error("get sqlId={} fail, exceptin={}"
//                    , sqlId, e.getMessage(), e);
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    public String getSQLSource(DalClient dalClient, String sqlId, Map<String, Object> paramMap) {
//        Configuration configuration = this.getConfiguration(dalClient);
//        MappedSqlExecutor mappedSqlExecutor = this.getMappedSqlExecutor(dalClient, sqlId, paramMap);
//        return this.getSQLSource(configuration, mappedSqlExecutor, sqlId, paramMap);
//    }
//
//    public <T> List<T> queryForList(MappedSqlExecutor mapped, String sql
//            , String sqlId, Map<String, Object> paramMap, Class<T> requiredType) {
//        if (mapped == null || paramMap == null
//                || StringUtils.isBlank(sql)
//                || StringUtils.isBlank(sqlId)) {
//            return null;
//        }
//
//        long startTimestamp = System.currentTimeMillis();
//        try {
//            Field field = ReflectionUtils.findField(mapped.getClass(), null, NamedParameterJdbcTemplate.class);
//            field.setAccessible(true);
//            NamedParameterJdbcTemplate execution = (NamedParameterJdbcTemplate)field.get(mapped);
//            List<?> resultList;
//            if (requiredType == null) {
//                resultList = execution.queryForList(sql, DalUtils.mapIfNull(paramMap));
//            } else {
//                resultList = execution.query(sql, DalUtils.mapIfNull(paramMap)
//                        , new RowMapperFactory(requiredType).getRowMapper());
//            }
//            return (List<T>) resultList;
//        } catch(Exception e) {
//            logger.error("queryForList exception, {}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        } finally {
//            this.logProfileLongTimeRunningSql(mapped, sql, paramMap, startTimestamp);
//        }
//    }
//
//    public <T> T queryForObject(MappedSqlExecutor mapped, String sql
//            , String sqlId, Map<String, Object> paramMap, Class<T> requiredType) {
//        if (mapped == null || paramMap == null
//                || StringUtils.isBlank(sql)
//                || StringUtils.isBlank(sqlId)) {
//            return null;
//        }
//
//        long startTimestamp = System.currentTimeMillis();
//        try {
//            Field field = ReflectionUtils.findField(mapped.getClass(), null, NamedParameterJdbcTemplate.class);
//            field.setAccessible(true);
//            NamedParameterJdbcTemplate execution = (NamedParameterJdbcTemplate)field.get(mapped);
//            List<T> resultList = execution.query(sql, paramMap, new RowMapperFactory(requiredType).getRowMapper());
//
//            Method method = ReflectionUtils.findMethod(mapped.getClass(), "singleResult", List.class);
//            method.setAccessible(true);
//            return (T) method.invoke(mapped, resultList);
//        } catch(Exception e) {
//            logger.error("queryForList exception, {}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        } finally {
//            this.logProfileLongTimeRunningSql(mapped, sql, paramMap, startTimestamp);
//        }
//    }
//
//    public Object execute(MappedSqlExecutor mapped, String sql
//            , String sqlId, Map<String, Object> paramMap) {
//        if (mapped == null || paramMap == null
//                || StringUtils.isBlank(sql)
//                || StringUtils.isBlank(sqlId)) {
//            return 0;
//        }
//
//        long startTimestamp = System.currentTimeMillis();
//        try {
//            Field field = ReflectionUtils.findField(mapped.getClass(), null, NamedParameterJdbcTemplate.class);
//            field.setAccessible(true);
//            NamedParameterJdbcTemplate execution = (NamedParameterJdbcTemplate) field.get(mapped);
//            return execution.execute(sql, paramMap, ps -> ps.executeUpdate());
//        } catch(Exception e) {
//            logger.error("queryForList exception, {}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        } finally {
//            this.logProfileLongTimeRunningSql(mapped, sql, paramMap, startTimestamp);
//        }
//    }
//
//    private void logProfileLongTimeRunningSql(MappedSqlExecutor mapped, String sql, Map<String, Object> paramMap
//            , long startTimestamp) {
//        try {
//            Method method = ReflectionUtils.findMethod(mapped.getClass(), "logProfileLongTimeRunningSql");
//            method.setAccessible(true);
//            method.invoke(mapped, startTimestamp, sql, paramMap);
//        } catch (Exception e) {
//            logger.error("queryForList exception, {}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    public List<Column> getColumns(String sql) {
//        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
//        String lowerSql = sql.toLowerCase();
//        lowerSql = lowerSql.split("select ")[1];
//        lowerSql = lowerSql.split(" from")[0];
//        lowerSql = StringUtils.strip(lowerSql);
//
//        List<Column> columnList = new ArrayList<>();
//        int start = 0;
//        int layer = 0;
//        boolean markFlag = false;
//        for (int i = 0; i < lowerSql.length(); i++) {
//            String ch = lowerSql.substring(i, i + 1);
//            if (ch.equals("(")) {
//                layer++;
//            } else if (ch.equals(")")) {
//                layer--;
//            } else if (ch.equals("'")) {
//                if (markFlag) {
//                    layer--;
//                } else {
//                    layer++;
//                }
//                markFlag = !markFlag;
//            } else if ((ch.equals(",") || i == (lowerSql.length() - 1)) && layer == 0) {
//                int end = i + (ch.equals(",") ? 0 : 1);
//                columnList.add(new Column(lowerSql.substring(start, end)));
//                start = i + 1;
//            }
//        }
//        return columnList;
//    }
//
//    public int getTableShardCount(String sql, String shardTblMethodName) {
//        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
//        if (!sql.contains("${" + shardTblMethodName)) {
//            return 0;
//        }
//        String lowerSql = sql.toLowerCase();
//        int beginIndex = lowerSql.indexOf("${" + shardTblMethodName);
//        int endIndex = lowerSql.indexOf(")", beginIndex);
//        beginIndex = lowerSql.lastIndexOf(",", endIndex);
//        String prismSql = lowerSql.substring(beginIndex + 1, endIndex).trim();
//        return Integer.parseInt(prismSql);
//    }
//
//    public int[] getLimits(String sql) {
//        int[] limits = {0, 0};
//
//        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
//        String lowerSql = sql.toLowerCase();
//        String[] splitSql = lowerSql.split(" limit ");
//        if (splitSql.length <= 1) {
//            return limits;
//        }
//
//        String[] limitsStr = splitSql[splitSql.length - 1].split(",");
//        if (limits.length > 1) {
//            limits[0] = Integer.parseInt(limitsStr[0].trim());
//            limits[1] = Integer.parseInt(limitsStr[1].trim());
//        } else {
//            limits[1] = Integer.parseInt(limitsStr[0].trim());
//        }
//        return limits;
//    }
//
//    public SortColumn[] getOrderByColumns(String sql) {
//        SortColumn[] cols = {new SortColumn("id", true)};
//
//        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
//        String lowerSql = sql.toLowerCase();
//        String[] splitSql = lowerSql.split(" order by ");
//        if (splitSql.length <= 1) {
//            return cols;
//        }
//
//        lowerSql = splitSql[splitSql.length - 1];
//        lowerSql = lowerSql.split(" limit ")[0];
//        lowerSql = lowerSql.replaceAll("\\)", "");
//        String[] columns = lowerSql.split(",");
//        cols = new SortColumn[columns.length];
//        for (int i = 0; i < columns.length; i++) {
//            String[] orderByColumn = columns[i].trim().split("[ ]+");
//            cols[i] = new SortColumn(orderByColumn[0], true);
//            if (orderByColumn.length > 1
//                    && "desc".equalsIgnoreCase(orderByColumn[1])) {
//                cols[i] = new SortColumn(orderByColumn[0], false);
//            }
//        }
//        return cols;
//    }
//
//    public Object getValue(Object obj, String column) {
//        if (obj == null) {
//            return null;
//        }
//
//        if (obj instanceof Map) {
//            return ((Map) obj).get(column);
//        }
//
//        Field[] fields = obj.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                // 1，原字段取值
//                if (field.getName().equals(column)) {
//                    return field.get(obj);
//                }
//                // 2，移除“_”，字段名改驼峰
//                String humpName = this.getHumpName(column);
//                if (field.getName().equals(humpName)) {
//                    return field.get(obj);
//                }
//                // 3，属性注解取值
//                javax.persistence.Column colAnno = field.getAnnotation(javax.persistence.Column.class);
//                if (colAnno != null && colAnno.name().equals(column)) {
//                    return field.get(obj);
//                }
//            } catch (Exception e) {
//                logger.error("对象字段取值异常, {}", e.getMessage(), e);
//            }
//        }
//
//        // 4，方法注解取值
//        Method[] methods = obj.getClass().getDeclaredMethods();
//        for (Method method : methods) {
//            try {
//                javax.persistence.Column colAnno = method.getAnnotation(javax.persistence.Column.class);
//                if (colAnno != null && colAnno.name().equals(column)) {
//                    return method.invoke(obj);
//                }
//            } catch (Exception e) {
//                logger.error("对象方法注解取值异常, {}", e.getMessage(), e);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * @description 获取驼峰名
//     * <p>〈功能详细描述〉</p>
//     *
//     * @auther  陈晨
//     * @date    2019/11/18 17:40
//     * @param   column
//     */
//    public String getHumpName(String column) {
//        String[] cols = column.split("_");
//        StringBuilder humpName = new StringBuilder();
//        for (String col : cols) {
//            String firstName = col.substring(0, 1);
//            String lastName = col.substring(1);
//            humpName.append(firstName.toUpperCase()).append(lastName);
//        }
//        String firstName = humpName.substring(0, 1);
//        String lastName = humpName.substring(1);
//        humpName = new StringBuilder();
//        humpName.append(firstName.toLowerCase()).append(lastName);
//        return humpName.toString();
//    }
//
//    public List<SortObject> sortMerge(List<SortObject> sortResultList
//            , List<?> mergeList, SortColumn[] sortColumns) {
//        if (CollectionUtils.isEmpty(mergeList)) {
//            return sortResultList;
//        }
//        for (Object obj : mergeList) {
//            // 获取排序字段值
//            SortColumn sortColumn = new SortColumn("null", true, obj.hashCode());
//            sortResultList.add(new SortObject(sortColumn, obj));
//        }
//        return sortResultList;
//
////        // 设置待排序数据集合
////        for (Object obj : mergeList) {
////            // 获取排序字段值
////            SortColumn sortColumn = null;
////            SortColumn lastSortColumn = null;
////            for (SortColumn col : sortColumns) {
////                if (col == null) {
////                    continue;
////                }
////                Object value = this.getValue(obj, col.getName());
////                if (value == null) {
////                    continue;
////                }
////                if (sortColumn == null) {
////                    sortColumn = new SortColumn(col.getName(), col.isAsc(), value);
////                    lastSortColumn = sortColumn;
////                    continue;
////                }
////                lastSortColumn.setNextSort(new SortColumn(col.getName(), col.isAsc(), value));
////                lastSortColumn = lastSortColumn.getNextSort();
////            }
////            // 无任何排序依据，则无序
////            if (sortColumn == null) {
////                sortColumn = new SortColumn("null", true, obj.hashCode());
////            }
////            sortResultList.add(new SortObject(sortColumn, obj));
////        }
////        Collections.sort(sortResultList);
////        return sortResultList;
//    }
//
//    public <T> List<T> limitLast(List<T> resultList, int[] limits) {
//        if (CollectionUtils.isEmpty(resultList)
//                || ArrayUtils.isEmpty(limits) || limits.length < 2) {
//            return resultList;
//        }
//        // 未达到数据上限
//        int max = limits[1];
//        if (limits[0] > 0) {
//            max = limits[0] + limits[1];
//        }
//        if (resultList.size() <= max) {
//            return resultList;
//        }
//        // 移除超出上限的数据
//        for (int i = 0; i < resultList.size() - max; i++) {
//            resultList.remove(max);
//        }
//        return resultList;
//    }
//
//    public synchronized List<SortObject> sortMergeAndLimitLast(List<SortObject> sortResultList
//            , List<?> mergeList, SortColumn[] sortColumns, int[] limits) {
//        this.sortMerge(sortResultList, mergeList, sortColumns);
//        this.limitLast(sortResultList, limits);
//        return sortResultList;
//    }
//
//    public <T> List<T> limit(List<T> resultList, int[] limits) {
//        if (CollectionUtils.isEmpty(resultList)
//                || ArrayUtils.isEmpty(limits) || limits.length < 2) {
//            return resultList;
//        }
//        // 计算上下限
//        int max = limits[1];
//        if (limits[0] > 0) {
//            max = limits[0] + limits[1];
//        }
//        int min = limits[0];
//        // 移除超出上限的数据
//        for (int i = 0; i < resultList.size() - max; i++) {
//            resultList.remove(max);
//        }
//        // 移除超出下限的数据
//        for (int i = 0; i < min; i++) {
//            resultList.remove(0);
//        }
//        return resultList;
//    }
//
//    public Object groupData(String sql, List<Object> resultList) {
//        return null;
//    }
//
//}
//
//
