package dynamo

import types.JobInfo
import awscala._, dynamodbv2._
import org.joda.time.format.ISODateTimeFormat
import constants.JobStatusTableConstants
import constants.JobStatusTableConstants.JobStatus
import constants.JobStatusTableConstants.JobStatus.JobStatus

/**
 * Created with IntelliJ IDEA.
 * User: cameron
 * Date: 4/14/14
 * Time: 5:09 PM
 * Accessor for the JobStatus table
 */
class JobStatusAccessor(val prefix: String) extends DynamoAccessor {
  implicit val const = JobStatusTableConstants

  var table: Table = dynamo.table(prefix + const.TABLE_NAME) get

  def addEntry(info: JobInfo) {
    val timestamp = DateTime.now().toString(ISODateTimeFormat.dateTime())

    table.putItem(info.jobId,
      const.FARM_ID -> info.farmId,
      const.CHANNEL -> info.channel,
      const.CHANNEL_VERSION -> info.channelVersion,
      const.MODULE -> info.module,
      const.MODULE_VERSION -> info.moduleVersion,
      const.STATUS -> JobStatus.Pending.toString,
      const.ADDED_AT -> timestamp,
      const.LAST_STATUS_CHANGE -> timestamp
    )
  }

  def updateStatus(jobId: Int, status: JobStatus) {
    val timestamp = DateTime.now().toString(ISODateTimeFormat.dateTime())

    table.putAttributes(jobId,
      Seq(const.STATUS -> status.toString,const.LAST_STATUS_CHANGE -> timestamp)
    )
  }
}
