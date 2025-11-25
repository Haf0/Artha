package com.haf.artha.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.haf.artha.data.local.model.TransactionType
import kotlinx.parcelize.Parcelize


//TODO if it's possible add new features for scheduled transaction
@Parcelize
@Entity(
    tableName = "scheduled_transactions",
    foreignKeys = [
        // Foreign Key to Account (Money comes from here)
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        // Foreign Key to Category
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        // Foreign Key to 'To Account' (for transfers)
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["to_account_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class ScheduledTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    // --- Standard Transaction Data (Copied from TransactionEntity) ---
    @ColumnInfo(name = "account_id", index = true)
    val accountId: Int,

    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "type")
    val type: TransactionType,

    @ColumnInfo(name = "to_account_id", index = true)
    val toAccountId: Int?,

    @ColumnInfo(name = "note")
    val note: String,

    // --- Scheduler Specific Columns ---

    // When should this transaction execute? (Time in millis)
    @ColumnInfo(name = "next_execution_date")
    val nextExecutionDate: Long,

    // How often does it repeat? 
    // Use an Enum or String constant (e.g., "DAILY", "WEEKLY", "MONTHLY", "NONE")
    @ColumnInfo(name = "frequency")
    val frequency: String,

    // Optional: Is the scheduler active? (allows pausing a recurring payment)
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    // Optional: When was this scheduler created?
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable