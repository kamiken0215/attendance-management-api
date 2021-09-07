# Attendance Management API (Unfinished)



## 共通事項

### 認証

全てのリクエストにBearer tokenが必要です。

### パラメータの型

|  パラメータ  |  型  |  内容  |
| ---- | ---- | ---- |
|  companyId  |  Integer  |  所属する会社のID  |
|  departmentCode  |  String  |  所属する会社の部門コード  |
|  userId  |  Integer  |  ユーザーID  |
|  attendanceCode  |  String  |  所属する会社の出勤区分コード  |
|  attendanceDate  |  String  |  YYYYMMDD  |

### GET/POSTメソッドのレスポンス

1. エラーがあればerrorパラメータにエラー文が格納されます。

```json
{
    "companyId"   : null,
    "companyName" : null,
    "error"       : "この会社名は既に使用されています"
}
```

2. その他エラーは空の配列が返ります。

### DELETEメソッドのレスポンス

以下のレスポンスで統一されています。

**Response:**

|  パラメータ  |  型  |  内容  |
| ---- | ---- | ---- |
|  number  |  String  |  処理対象データの件数  |
|  message  |  Integer  |  エラーメッセージ  |
|  ok  |  Boolean  |  true(Success) / false(Error)  |


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


## Company

### GET

所属している会社情報を取得できます

**Request:**

`companies/{companyId}`

**Data:**

none

**Response:**

Example:
`companies/1`

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
    ],
    "error" : ""
  }
]
```

###POST


所属している会社の会社名を変更できます。
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
    "companyName" : "sample",
    "error"       : ""
}
```

###DELETE

所属している会社を削除できます。
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

共通事項 DELETE 参照

<a id="department"></a>

## Department

### GET

所属している会社の部門情報を取得できます

- 部門一覧

**Request:**

`companies/{companyId}/departments`

**Data:**

none

**Response:**

Example:
`companies/1/departments`

```json
{
  "departments" : [
      {
        "companyId"       : 1,
        "departmentCode"  : 101,
        "departmentName"  : "管理部"
      },
      {
        "companyId"       : 1,
        "departmentCode"  : 102,
        "departmentName"  : "営業部"
      }
  ],
  "error" : ""
}
```


- 部門ごと

**Request:**

`companies/{companyId}/departments/{departmentCode}`

**Data:**

none

**Response:**

Example:
`companies/1/departments/101`

```json
[
    {
      "companyId"       : 1,
      "departmentCode"  : 101,
      "departmentName"  : "管理部",
      "error"           : ""
    }
]
```

###POST

1. 追加

所属している会社の部門名を追加できます。
編集権限を所有している必要があります

**Request:**

`/departments`

**Data:**

```json
[
  {
    "companyId"       : 1,
    "departmentCode"  : "201",
    "departmentName"  : "総務部"
  },
  {
    "companyId"       : 1,
    "departmentCode"  : "301",
    "departmentName"  : "経理部"
  }
]
```

**Response:**

```json
{
    "departments" : [
      {
        "companyId"       : 1,
        "departmentCode"  : "201",
        "departmentName"  : "総務部"
      },
      {
        "companyId"       : 1,
        "departmentCode"  : "301",
        "departmentName"  : "経理部"
      }
    ],
    "error" : ""
}
```

2. 編集
   所属している会社の部門名を変更できます。
   編集権限を所有している必要があります

**Request:**

`/departments`

**Data:**

```json
{
  "companyId"       : 1,
  "departmentCode"  : "101",
  "departmentName"  : "CHANGE"
}
```

**Response:**

```json
{
  "companyId"       : 1,
  "departmentCode"  : "101",
  "departmentName"  : "CHANGE",
  "error"           : ""
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

1. 全部門削除
   `companies/{companyId}/departments`

2. 各部門削除
   `companies/{companyId}/departments/{departmentCode}`

**Data:**

none

**Response:**

共通事項 DELETE 参照

