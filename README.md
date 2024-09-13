# QnbSanalPOS
Qnb Finansbank SanalPOS entegrasyonu


### Ödeme isteği:
* Kullanıcıdan alınacak ücret için kullanılan API'dir. 
* ```GET http://localhost:8080/initiate?amount=61.00```

### İptal isteği
* Ödeme işlemi üzerinden 24 saat geçmediyse iadesi için kullanılacak API'dir.
* ```GET http://localhost:8080/cancel?orderId=89c8acdf-7986-4899-a9d6-edde016b68fa```

### İade isteği
* Ödeme işlemi üzerinden 24 saat geçmiş ise iadesi için kullanılacak API'dir.
* ```GET http://localhost:8080/refund?orderId=89c8acdf-7986-4899-a9d6-edde016b68fa&amount=61.00```

### Ödeme doğrulama isteği
* Ödeme işlemi sonucunu banka tarafından sorgulamak için kullanılacak API'dir. 
```GET http://localhost:8080/inquiry?orderId=89c8acdf-7986-4899-a9d6-edde016b68fa```



Detaylı bilgi için bkz: https://vpos.qnbfinansbank.com/Help/home