package com.haf.artha.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "transaction",
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
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val transactionId: Int? = null,
    @ColumnInfo(name = "account_id", index = true)
    val accountId: Int,
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int,
    @ColumnInfo(name = "name")
    val transactionName: String,
    @ColumnInfo(name = "date")
    val transactionDate: String,
    @ColumnInfo(name = "type")
    val transactionType: String,
    @ColumnInfo(name = "note")
    val transactionNote: String,
    @ColumnInfo(name = "amount")
    val transactionAmount: Double
): Parcelable
