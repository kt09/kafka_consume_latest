package org.jsmart.zerocode.core.kafka.consume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

import static org.jsmart.zerocode.core.kafka.KafkaConstants.RAW;

//@JsonIgnoreProperties(ignoreUnknown = true) //<--- Do not enable this. All properties need to be aware of and processed
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ConsumerLocalConfigs {
    private final String recordType;
    private final String fileDumpTo;
    // --------------------------------------------------------
    // Now it is unused as recordType decides the record format
    // TODO- remove this
    // --------------------------------------------------------
    private final String fileDumpType;
    private final Boolean commitAsync;
    private final Boolean commitSync;
    private final Boolean showConsumedRecords;
    private final Integer maxNoOfRetryPollsOrTimeouts;
    private final Long pollingTime;

    @JsonCreator
    public ConsumerLocalConfigs(
            @JsonProperty("recordType") String recordType,
            @JsonProperty("fileDumpTo") String fileDumpTo,
            @JsonProperty("fileDumpType") String fileDumpType,
            @JsonProperty("commitAsync") Boolean commitAsync,
            @JsonProperty("commitSync") Boolean commitSync,
            @JsonProperty("showConsumedRecords") Boolean showConsumedRecords,
            @JsonProperty("maxNoOfRetryPollsOrTimeouts") Integer maxNoOfRetryPollsOrTimeouts,
            @JsonProperty("pollingTime") Long pollingTime) {
        this.recordType = recordType;
        this.fileDumpTo = fileDumpTo;
        this.fileDumpType = fileDumpType;
        this.commitAsync = commitAsync;
        this.commitSync = commitSync;
        this.showConsumedRecords = showConsumedRecords;
        this.maxNoOfRetryPollsOrTimeouts = maxNoOfRetryPollsOrTimeouts;
        this.pollingTime = pollingTime;
    }

    public String getRecordType() {
        return recordType != null ? recordType : RAW;
    }


    public String getFileDumpTo() {
        return fileDumpTo;
    }

    // --------------------------------------------------------
    // Now it is unused as recordType decides the record format
    // --------------------------------------------------------
    public String getFileDumpType() {
        return fileDumpType;
    }

    public Boolean getCommitAsync() {
        return commitAsync;
    }

    public Boolean getCommitSync() {
        return commitSync;
    }

    public Boolean getShowConsumedRecords() {
        return showConsumedRecords != null ? showConsumedRecords : true;
    }

    public Integer getMaxNoOfRetryPollsOrTimeouts() {
        return maxNoOfRetryPollsOrTimeouts;
    }

    public Long getPollingTime() {
        return pollingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerLocalConfigs that = (ConsumerLocalConfigs) o;
        return Objects.equals(recordType, that.recordType) &&
                Objects.equals(fileDumpTo, that.fileDumpTo) &&
                Objects.equals(fileDumpType, that.fileDumpType) &&
                Objects.equals(commitAsync, that.commitAsync) &&
                Objects.equals(commitSync, that.commitSync) &&
                Objects.equals(showConsumedRecords, that.showConsumedRecords) &&
                Objects.equals(maxNoOfRetryPollsOrTimeouts, that.maxNoOfRetryPollsOrTimeouts) &&
                Objects.equals(pollingTime, that.pollingTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(recordType, fileDumpTo, fileDumpType, commitAsync, commitSync, showConsumedRecords, maxNoOfRetryPollsOrTimeouts, pollingTime);
    }

    @Override
    public String toString() {
        return "ConsumerLocalConfigs{" +
                "recordType='" + recordType + '\'' +
                ", fileDumpTo='" + fileDumpTo + '\'' +
                ", fileDumpType='" + fileDumpType + '\'' +
                ", commitAsync=" + commitAsync +
                ", commitSync=" + commitSync +
                ", showConsumedRecords=" + showConsumedRecords +
                ", maxNoOfRetryPollsOrTimeouts=" + maxNoOfRetryPollsOrTimeouts +
                ", pollingTime=" + pollingTime +
                '}';
    }
}
