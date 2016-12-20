## Pivotal Acceleration Lab project demonstrating the process of refactoring of local file persistence to be 12 factor compliant.

### Run simple java app
1. Checkout _v2.0_

>git checkout v2.0

2. Compile and run app specifying following properties

```
--spring.profiles.active=S3 to save file to AWS, or --spring.profiles.active=local
--cloud.aws.credentials.accessKey={s3 key}
--cloud.aws.credentials.secretKey={s3 secret}
--cloud.aws.s3.bucket={s3 bucket}
```

### Step description
In this step, funcionality to save, list, and detele file was refactored out into _AwsFileOperations.java_
and _LocalFileOperations.java_ beans.  Specifying the profile when running an application will inject the bean
with appropriate storage functionality.

AWS dependencies added to pom.xml

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-aws-context</artifactId>
    <version>${spring.cloud.version}</version>
</dependency>
```

