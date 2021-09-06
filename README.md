# Attendance Management API (Unfinished)

## Company

### GET

所属している会社情報を取得できます

**Request:**

`companies/{companyId}`

**Data:**

none

**Response:**

```json
[
  {
    "companyId"   : 1,
    "companyName" : "sample",
    "departments" : [
      {
        "departmentCode"  : 101,
        "departmentName"  : "管理部"
      },
      {
        "departmentCode"  : 102,
        "departmentName"  : "営業部"
      }
    ]
  }
]
```

###POST

所属している会社の会社名を変更できます。<br>
編集権限を所有している必要があります

**Request:**

`/companies`

**Data:**

```json
{
  "companyId"   : 1,
  "companyName" : "sample"
}
```

**Response:**

```json
{
    "companyId"   : 1,
    "companyName" : "sample"
}
```

###DELETE
所属している会社を削除できます。<br>
削除するためには、紐づいている以下のデータを先に削除する必要があります。
- 出退勤データ
- 出退勤区分
- 部門データ
- ユーザー(ルートユーザー以外)

**Request:**

`companies/{companyId}`

**Data:**

none

**Response:**

|  プロパティ  |  型  |  内容  |
| ---- | ---- | ---- |
|  number  |  String  |  処理対象データの件数  |
|  message  |  Int  |  エラーメッセージ  |
|  ok  |  Bool  |  true(Success) or false(Error)  |


Success

```json
{
    "number"  : 1,
    "message" : "",
    "ok"      : true
}
```

Failure (この例では、会社に紐づくデータがまだ削除されていない場合を示しています)

```json
{
    "number"  : 1,
    "message" : "関連するデータを先に削除してください",
    "ok"      : false
}
```