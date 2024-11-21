package com.haf.artha.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.haf.artha.data.local.model.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["to_account_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val transactionId: Int = 0,
    @ColumnInfo(name = "account_id", index = true)
    val accountId: Int,
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "type")
    val type: TransactionType,
    @ColumnInfo(name = "to_account_id", index = true)
    val toAccountId: Int?,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "amount")
    val amount: Double
): Parcelable
