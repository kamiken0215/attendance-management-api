# Attendance Management API



## 共通事項

### 認証

全てのリクエストにBearer tokenが必要です。

### 権限一覧

|  権限コード  |  COMPANY  |  DEPARTMENT  |  USER  |  ATTENDANCE CLASS  |  ATTENDANCE  |
|  -------   |  -------   |  -------   |  -------   |  -------   |  -------   |
|  0777      | 照会/編集/削除 | 照会/編集/削除 | 照会/編集/削除 | 照会/編集/削除 | 照会/編集/削除 |
|  0677      | 照会/編集 | 照会/編集/削除 | 照会/編集/削除 | 照会/編集/削除 | 照会/編集/削除 |
|  0077      | なし     | なし          | 照会/編集/削除 | 照会          | 照会/編集/削除 |
|  0067      | なし     | なし         | 照会/編集      | 照会           | 照会/編集/削除 |
|  0047      | なし     | なし         | 照会          | 照会           | 照会/編集/削除 |
|  0007      | なし     | なし         | なし          | 照会           | 照会/編集/削除 |
|  0006      | なし     | なし         | なし          | なし           | 照会/編集 |
|  0004      | なし     | なし         | なし          | なし           | 照会 |

### パラメータの型

|  パラメータ  |  型  |  内容  |
| ---- | ---- | ---- |
|  companyId  |  Integer  |  所属する会社のID  |
|  departmentCode  |  String  |  所属する会社の部門コード  |
|  userId  |  Integer  |  ユーザーID  |
|  attendanceCode  |  String  |  所属する会社の出勤区分コード  |
|  attendanceDate  |  String  |  YYYYMMDD  |

### GETメソッドのレスポンス

1. エラーがあればerrorパラメータにエラー文が格納されます。なければerrorはnullとなります。

```json
{
    "companyId"   : null,
    "companyName" : null,
    "error"       : "情報を取得できませんでした"
}
```

```json
{
    "companyId"   : 1,
    "companyName" : "test",
    "error"       : null
}
```


### POST/DELETEメソッドのレスポンス

以下のレスポンスで統一されています。

**Response:**

|  パラメータ  |  型  |  内容  |
| ---- | ---- | ---- |
|  number  |  String  |  処理対象データの件数  |
|  message  |  Integer  |  エラーメッセージ  |
|  ok  |  Boolean  |  true(success) / false(error)  |


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
    "companyId": 1,
    "companyName": "sample",
    "departments": [
      {
        "departmentCode": 101,
        "departmentName": "管理部"
      },
      {
        "departmentCode": 102,
        "departmentName": "営業部"
      }
    ],
    "error": null
  }
]
```

### POST


所属している会社の会社名を変更できます。
編集権限を所有している必要があります

**Request:**

`/companies`

**Data:**

```json
{
  "companyId": 1,
  "companyName": "sample"
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

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

共通事項　POST/DELETEメソッドのレスポンス参考

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
  "departments": [
      {
        "companyId": 1,
        "departmentCode": 101,
        "departmentName": "管理部"
      },
      {
        "companyId": 1,
        "departmentCode": 102,
        "departmentName": "営業部"
      }
  ],
  "error" : null
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
      "companyId": 1,
      "departmentCode": 101,
      "departmentName": "管理部",
      "error": null
    }
]
```

### POST

- 追加

所属している会社の部門名を追加できます。
編集権限を所有している必要があります

**Request:**

`/departments`

**Data:**

```json
[
  {
    "companyId": 1,
    "departmentCode": "201",
    "departmentName": "総務部"
  },
  {
    "companyId": 1,
    "departmentCode": "301",
    "departmentName": "経理部"
  }
]
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

- 編集


所属している会社の部門名を変更できます。
編集権限を所有している必要があります

**Request:**

`/departments`

**Data:**

```json
{
  "companyId": 1,
  "departmentCode": "101",
  "departmentName": "CHANGE"
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

### DELETE

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

共通事項　POST/DELETEメソッドのレスポンス参考

<a id="department"></a>

## USER

### GET

所属している会社のユーザーの情報を取得できます

- 会社に属するユーザー一覧

**Request:**

`companies/{companyId}/users`

**Data:**

none

**Response:**

Example:
`companies/1/users`

```json
{
    "users": [
        {
            "userId": 33,
            "userName": "gg",
            "email": "33@gmail.com",
            "paidHolidays": 0,
            "isActive": "on",
            "companyId": 3,
            "companyName": "tt",
            "departmentCode": "101",
            "departmentName": "管理部門3",
            "roleCode": "7777",
            "roleName": "ROLE_ADMIN"
        },
        {
            "userId": 34,
            "userName": "delete",
            "email": "34@gmail.com",
            "paidHolidays": 0,
            "isActive": "on",
            "companyId": 3,
            "companyName": "tt",
            "departmentCode": "102",
            "departmentName": "総務部3",
            "roleCode": "7777",
            "roleName": "ROLE_ADMIN"
        }
    ],
    "error": null
}
```


- 部門ごと

`companies/{companyId}/departments/{departmentCode}/users`

- 単一ユーザー

`companies/{companyId}/departments/{departmentCode}/users/{userId}`

### POST
- 追加

所属している会社のユーザーを追加できます。
編集権限を所有している必要があります。
ユーザーIDは不要です(自動採番)

**Request:**

`/users`

**Data:**

```json
{
    "companyId": 1,
    "users":[
        {
            "companyId":1,
            "userName": "addtest1",
            "email": "ins3@gmail.com",
            "password":"test",
            "paidHolidays": 0,
            "isActive": "off",
            "departmentCode": "101",
            "departmentName": "管理部門3",
            "roleCode": "7777"
        },
        {
            "companyId":1,
            "userName": "addtest2",
            "email": "ins4@gmail.com",
            "password":"test",
            "paidHolidays": 0,
            "isActive": "off",
            "departmentCode": "101",
            "departmentName": "管理部門3",
            "roleCode": "7777"
        }
    ]
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

- 編集


所属している会社の部門名を変更できます。
編集権限を所有している必要があります
passwordを変更しない場合はpasswordパラメータは不要です。

**Request:**

`/users`

**Data:**

2件目のユーザーはパスワードを変更しない例です。

```json
{
    "companyId": 1,
    "users":[
        {
            "companyId":1,
            "userId":40,
            "userName": "addtest1",
            "email": "upd3@gmail.com",
            "password":"test",
            "paidHolidays": 0,
            "isActive": "off",
            "departmentCode": "101",
            "departmentName": "管理部門3",
            "roleCode": "7777"
        },
        {
            "companyId":1,
            "userId":41,
            "userName": "addtest2",
            "email": "upd@gmail.com",
            "paidHolidays": 0,
            "isActive": "off",
            "departmentCode": "101",
            "departmentName": "管理部門3",
            "roleCode": "7777"
        }
    ]
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

### DELETE

所属している会社を削除できます。<br>
削除するためには、紐づいている以下のデータを先に削除する必要があります。
- 出退勤データ
- 出退勤区分
- 部門データ
- ユーザー(ルートユーザー以外)

**Request:**

1. 会社ごとに削除

`companies/{companyId}/users`

2. 部門ごとに削除

`companies/{companyId}/departments/{departmentCode}/users`

3. 各ユーザー削除

`companies/{companyId}/departments/{departmentCode}/users/{userId}`

**Data:**

none

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

## Role

権限一覧を取得できます。
GETのみ提供されています。
(要Bearer token)

### GET

**Request:**

`/roles`

**Data:**

none

**Response:**

```json
{
   "roles": [
      {
         "roleCode": "7777",
         "roleName": "ROLE_ADMIN",
         "explanation": "全ての実行権限あり"
      },
      {
         "roleCode": "0777",
         "roleName": "ROLE_COMPANY",
         "explanation": "会社情報の設定以下の全権限"
      },
      {
         "roleCode": "0677",
         "roleName": "ROLE_COMPANY",
         "explanation": "会社情報の編集以下の全権限"
      },
      {
         "roleCode": "0077",
         "roleName": "ROLE_USER",
         "explanation": "ユーザーの設定以下の全権限"
      },
      {
         "roleCode": "0067",
         "roleName": "ROLE_USER",
         "explanation": "ユーザーの登録/編集以下の権限"
      },
      {
         "roleCode": "0047",
         "roleName": "ROLE_USER",
         "explanation": "ユーザーの参照"
      },
      {
         "roleCode": "0007",
         "roleName": "ROLE_ATTENDANCE",
         "explanation": "出勤データの設定以下の全権限"
      },
      {
         "roleCode": "0006",
         "roleName": "ROLE_ATTENDANCE",
         "explanation": "自分の出勤データの登録/編集のみ"
      },
      {
         "roleCode": "0004",
         "roleName": "ROLE_ATTENDANCE",
         "explanation": "自分の出勤データの参照のみ"
      }
   ],
   "error": null
}
```


## ATTENDANCE STATUS

出勤データのステータス一覧を取得できます。
GETのみ提供されています。
(要Bearer token)

### GET

**Request:**

`/status`

**Data:**

none

**Response:**

```json
{
   "attendanceStatuses": [
      {
         "attendanceStatusCode": "101",
         "attendanceStatusName": "on"
      },
      {
         "attendanceStatusCode": "102",
         "attendanceStatusName": "keep"
      },
      {
         "attendanceStatusCode": "201",
         "attendanceStatusName": "off"
      },
      {
         "attendanceStatusCode": "202",
         "attendanceStatusName": "reject"
      }
   ],
   "error": null
}
```

## ATTENDANCE CLASS

### GET

所属している会社の勤怠区分の情報を取得できます

- 勤怠区分一覧

**Request:**

`/companies/{companyId}/classes`

**Data:**

none

**Response:**

Example:
`companies/1/classes`

```json
{
    "attendanceClasses": [
        {
            "companyId": 1,
            "attendanceClassCode": "101",
            "attendanceClassName": "出勤3",
            "startTime": "08:00:00",
            "endTime": "17:00:00"
        },
        {
            "companyId": 1,
            "attendanceClassCode": "102",
            "attendanceClassName": "フレックス",
            "startTime": "09:00:00",
            "endTime": "18:00:00"
        }
    ],
    "error": null
}
```

- 単一区分

`/companies/{companyId}/classes/{attendanceClassCode}`

### POST
- 追加

所属している会社の勤怠区分を追加できます。
編集権限を所有している必要があります。

**Request:**

`/classes`

**Data:**

```json
{
    "companyId": 1,
    "attendanceClasses":[
        {
            "companyId": 1,
            "attendanceClassCode": "998",
            "attendanceClassName": "出勤998",
            "startTime": "08:00:00",
            "endTime": "17:00:00"
        },
            {
            "companyId": 1,
            "attendanceClassCode": "999",
            "attendanceClassName": "test",
            "startTime": "08:00:00",
            "endTime": "17:00:00"
        }
    ]
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

- 編集


所属している会社の区分情報を変更できます。
編集権限を所有している必要があります

**Request:**

`/classes`

**Data:**

追加と同じ

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

### DELETE

所属している会社を削除できます。<br>
削除するためには、紐づいている以下のデータを先に削除する必要があります。
- 出退勤データ

**Request:**

1. 会社ごとに削除

`/companies/{companyId}/classes`

2. 区分ごとに削除

`/companies/{companyId}/classes/{attendanceClassCode}`

**Data:**

none

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

## ATTENDANCE DATE

### GET

所属している会社のユーザーの勤怠情報を取得できます

- 勤怠区分一覧

**Request:**

`companies/{companyId}/attendances`

**Data:**

none

**Response:**

Example:
`companies/1/attendances`

```json
[
    {
        "userId": 1,
        "attendances": [
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 1,
                "userName": "test1",
                "attendanceDate": "20200123",
                "startTime": "2020-01-23 09:52:31",
                "endTime": "2020-01-23 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            },
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 1,
                "userName": "test1",
                "attendanceDate": "20210624",
                "startTime": "2021-06-24 09:52:31",
                "endTime": "2021-06-24 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            }
        ],
        "error": ""
    },
    {
        "userId": 2,
        "attendances": [
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 2,
                "userName": "test2",
                "attendanceDate": "20200701",
                "startTime": "2020-07-01 09:52:31",
                "endTime": "2020-07-01 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            },
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 2,
                "userName": "test2",
                "attendanceDate": "20210822",
                "startTime": "2021-08-22 09:52:31",
                "endTime": "2021-08-22 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            }
        ],
        "error": ""
    }
]
```

- 部門毎

`companies/{companyId}/departments/{departmentCode}/attendances`

- ユーザーごと

`companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances`

- 日毎

`companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances/{attendanceDate}`

attendanceDateは前方一致です。
例えば2021年のデータを全て取得したい場合はattendanceDateを2021に、
2021年7月のデータを取得したい場合は202107、
2021年7月22日のデータを取得したい場合は20210722としてください。


**Response:**

Example:
`companies/1/departments/101/users/1/attendances/2021`

```json
[
    {
        "userId": 1,
        "attendances": [
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 1,
                "userName": "test1",
                "attendanceDate": "20210822",
                "startTime": "2021-08-22 09:52:31",
                "endTime": "2021-08-22 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            },
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 1,
                "userName": "test1",
                "attendanceDate": "20210824",
                "startTime": "2021-08-24 09:52:31",
                "endTime": "2021-08-24 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            }
        ],
        "error": ""
    }
]
```

Example:
`companies/1/departments/101/users/1/attendances/20210824`

```json
[
    {
        "userId": 1,
        "attendances": [
            {
                "companyId": 1,
                "departmentCode": "101",
                "userId": 1,
                "userName": "test1",
                "attendanceDate": "20210824",
                "startTime": "2021-08-24 09:52:31",
                "endTime": "2021-08-24 18:52:31",
                "attendanceClassCode": "101",
                "attendanceClassName": "出勤3",
                "attendanceStatusCode": "101",
                "attendanceStatusName": "on"
            }
        ],
        "error": ""
    }
]
```


### POST
- 追加

所属している会社のユーザーの勤怠を追加できます。
編集権限を所有している必要があります。

**Request:**

`/attendances`

**Data:**

```json
{
    "companyId": 1,
    "attendances": [
        {
            "userId": 1,
            "attendanceDate": "20210901",
            "startTime": "2021-09-01 09:52:31",
            "endTime": "2021-09-01 18:52:31",
            "attendanceClassCode": "101",
            "attendanceStatusCode": "101"
        },
        {
            "userId": 33,
            "attendanceDate": "20210902",
            "startTime": "2021-09-02 09:52:31",
            "endTime": "2021-09-02 18:52:31",
            "attendanceClassCode": "101",
            "attendanceStatusCode": "101"
        }
    ]
}
```

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

- 編集


所属している会社のユーザーの出勤データを変更できます。
編集権限を所有している必要があります

**Request:**

`/attendances`

**Data:**

追加と同じ

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

### DELETE

所属している会社のユーザーの出勤データを削除できます。<br>
削除するためには、紐づいている以下のデータを先に削除する必要があります。
- 出退勤データ

**Request:**

1. 会社ごとに削除

`companies/{companyId}/attendances`

2. 部門ごとに削除

`companies/{companyId}/departments/{departmentCode}/attendances`

3. ユーザーごとに削除

`companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances`

4. 日毎に削除

`companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances/{attendanceDate}`

**Data:**

none

**Response:**

共通事項　POST/DELETEメソッドのレスポンス参考

