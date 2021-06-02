package com.idit.designer.app.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.transactions.TransactionDurabilityLevel;
import com.couchbase.transactions.Transactions;
import com.couchbase.transactions.config.TransactionConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseDocument;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;
import com.idit.designer.project_manager.model.Project;


import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CouchbaseConfig.class);
    private static final String EPOC_MILLI = "epochTimeMilli";
    private static final String OFFSET_SECONDS = "offsetSeconds";

    @Value("${app.couchbase.connection-string}")
    private String connectionString;

    @Value("${app.couchbase.user-name}")
    private String userName;

    @Value("${app.couchbase.password}")
    private String password;

    @Value("${app.couchbase.bucket-product}")
    private String productBucket;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return productBucket;
    }

    @Override
    public CouchbaseCustomConversions customConversions() {
        return new CouchbaseCustomConversions(Arrays.asList(
                ZonedDateTimeToEpochTimeConverter.INSTANCE,
                EpochTimeToZonedDateTimeConverter.INSTANCE));
    }


    @WritingConverter
    public enum ZonedDateTimeToEpochTimeConverter implements Converter<ZonedDateTime, CouchbaseDocument> {
        INSTANCE;

        @Override
        public CouchbaseDocument convert(ZonedDateTime zonedDateTime) {
            CouchbaseDocument cd = new CouchbaseDocument();
            cd.put(EPOC_MILLI, zonedDateTime.toInstant().toEpochMilli());
            cd.put(OFFSET_SECONDS, zonedDateTime.getOffset().getTotalSeconds());
            return cd;
        }
    }

    @ReadingConverter
    public enum EpochTimeToZonedDateTimeConverter implements Converter<CouchbaseDocument, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(CouchbaseDocument epochTime) {
            long timeMilli = Long.parseLong(epochTime.getContent().get(EPOC_MILLI).toString());
            int offsetSeconds = Integer.parseInt(epochTime.getContent().get(OFFSET_SECONDS).toString());

            ZoneOffset convertedOffset = ZoneOffset.ofTotalSeconds(offsetSeconds);
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeMilli), convertedOffset);
        }
    }

   

    private CouchbaseCustomConversions customBucketsConversions() {
        return new CouchbaseCustomConversions(Arrays.asList(
                ZonedDateTimeToEpochTimeConverter.INSTANCE,
                EpochTimeToZonedDateTimeConverter.INSTANCE));
    }


    public CouchbaseTemplate customCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter) {
        return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
    }

    public CouchbaseClientFactory customCouchbaseClientFactory(String bucketName) {
        return new SimpleCouchbaseClientFactory(getConnectionString(), authenticator(), bucketName);
    }
}
