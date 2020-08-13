package uk.nhs.nhsx.diagnosiskeyssubmission;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.junit.Test;
import uk.nhs.nhsx.TestData;
import uk.nhs.nhsx.analyticssubmission.FakeS3Storage;
import uk.nhs.nhsx.core.aws.dynamodb.AwsDynamoClient;
import uk.nhs.nhsx.core.aws.s3.BucketName;
import uk.nhs.nhsx.core.aws.s3.ObjectKey;
import uk.nhs.nhsx.core.aws.s3.ObjectKeyNameProvider;
import uk.nhs.nhsx.diagnosiskeyssubmission.model.ClientTemporaryExposureKey;
import uk.nhs.nhsx.diagnosiskeyssubmission.model.ClientTemporaryExposureKeysPayload;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class DiagnosisKeysSubmissionServiceTest {

    private final BucketName BUCKET_NAME = BucketName.of("some-bucket-name");
    private final String uuid = "dd3aa1bf-4c91-43bb-afb6-12d0b5dcad43";
    private final FakeS3Storage s3Storage = new FakeS3Storage();
    private final AwsDynamoClient awsDynamoClient = mock(AwsDynamoClient.class);
    private final ObjectKeyNameProvider objectKeyNameProvider = mock(ObjectKeyNameProvider.class);
    private final String tableName = "some-table-name";
    private final String hashKey = "diagnosisKeySubmissionToken";
    private final DiagnosisKeysSubmissionService service = new DiagnosisKeysSubmissionService(s3Storage, awsDynamoClient, objectKeyNameProvider, tableName, BUCKET_NAME);
    private final ObjectKey objectKey = ObjectKey.of("some-object-key");

    private final Item dynamoItem = Item.fromJSON("{\"" + hashKey + "\": \"" + uuid + "\"}");

    @Test
    public void acceptsTemporaryExposureKeys() throws IOException {
        when(objectKeyNameProvider.generateObjectKeyName()).thenReturn(objectKey);
        when(awsDynamoClient.getItem(tableName, hashKey, uuid)).thenReturn(dynamoItem);

        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            asList(
                new ClientTemporaryExposureKey("W2zb3BeMWt6Xr2u0ABG32Q==", 12345, 144),
                new ClientTemporaryExposureKey("kzQt9Lf3xjtAlMtm7jkSqw==", 12499, 144)
            )
        );

        service.acceptTemporaryExposureKeys(payload);
        verifyHappyPath(TestData.STORED_KEYS_PAYLOAD);
    }

    @Test
    public void acceptsTemporaryExposureKeysWithEmptyArray() throws IOException {
        when(objectKeyNameProvider.generateObjectKeyName()).thenReturn(objectKey);
        when(awsDynamoClient.getItem(tableName, hashKey, uuid)).thenReturn(dynamoItem);

        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            emptyList()
        );

        service.acceptTemporaryExposureKeys(payload);

        final String expectedStoredPayload = "{\"temporaryExposureKeys\":[]}";
        verifyHappyPath(expectedStoredPayload);
    }

    @Test
    public void ifTokenDoesNotMatchThenKeysAreNotStored() {
        when(objectKeyNameProvider.generateObjectKeyName()).thenReturn(objectKey);
        when(awsDynamoClient.getItem(tableName, hashKey, uuid)).thenReturn(null);

        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            asList(
                new ClientTemporaryExposureKey("W2zb3BeMWt6Xr2u0ABG32Q==", 12345, 144),
                new ClientTemporaryExposureKey("kzQt9Lf3xjtAlMtm7jkSqw==", 12499, 144)
            )
        );

        service.acceptTemporaryExposureKeys(payload);

        verify(awsDynamoClient, times(1)).getItem(tableName, hashKey, uuid);

        assertThat(s3Storage.count, equalTo(0));
        verify(awsDynamoClient, times(0)).deleteItem(any(), any(), any());
    }

    @Test
    public void keyMustBeNonNull() {
        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            singletonList(
                new ClientTemporaryExposureKey(null, 12345, 144)
            )
        );

        service.acceptTemporaryExposureKeys(payload);

        verifyNoInteractions();
    }

    @Test
    public void keyMustBeBase64Encoded() {
        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            singletonList(
                new ClientTemporaryExposureKey("some-key", 12499, 144)
            )
        );

        service.acceptTemporaryExposureKeys(payload);

        verifyNoInteractions();
    }

    @Test
    public void rollingStartNumberMustBeNonNegative() {
        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            singletonList(
                new ClientTemporaryExposureKey("kzQt9Lf3xjtAlMtm7jkSqw==", -1, 144)
            )
        );

        service.acceptTemporaryExposureKeys(payload);

        verifyNoInteractions();
    }

    @Test
    public void rollingPeriodNumberMustBeExactly144() {
        ClientTemporaryExposureKeysPayload payload = new ClientTemporaryExposureKeysPayload(
            UUID.fromString(uuid),
            singletonList(
                new ClientTemporaryExposureKey("W2zb3BeMWt6Xr2u0ABG32Q==", 12345, 143)
            )
        );

        service.acceptTemporaryExposureKeys(payload);

        verifyNoInteractions();
    }

    private void verifyNoInteractions() {
        verify(objectKeyNameProvider, times(0)).generateObjectKeyName();
        verify(awsDynamoClient, times(0)).getItem(any(), any(), any());
        assertThat(s3Storage.count, equalTo(0));
        verify(awsDynamoClient, times(0)).deleteItem(any(), any(), any());

        verifyNoMoreInteractions(objectKeyNameProvider);
        verifyNoMoreInteractions(awsDynamoClient);
    }

    private void verifyHappyPath(String expectedStoredPayload) throws IOException {
        verify(objectKeyNameProvider, times(1)).generateObjectKeyName();
        verify(awsDynamoClient, times(1)).getItem(tableName, hashKey, uuid);

        assertThat(s3Storage.count, equalTo(1));
        assertThat(s3Storage.name, equalTo(objectKey.append(".json")));
        assertThat(s3Storage.bucket, equalTo(BUCKET_NAME));
        assertThat(s3Storage.bytes.read(), equalTo(expectedStoredPayload.getBytes(StandardCharsets.UTF_8)));
        verify(awsDynamoClient, times(1)).deleteItem(tableName, hashKey, uuid);

        verifyNoMoreInteractions(objectKeyNameProvider);
        verifyNoMoreInteractions(awsDynamoClient);
    }

}