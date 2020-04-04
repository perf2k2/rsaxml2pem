# RSAXML2PEM (XML TO PEM CONVERTING)

Java utility for converting xml rsa key (private/public) to pem key. Based
on good article and code by Matouš Borák http://www.platanus.cz/blog/converting-rsa-xml-key-to-pem

### Example keys

XML key:
```
<RSAKeyValue>
  <Modulus>wzZYQpFhIItfo5...3CZXgAyOc+w==</Modulus>
  <Exponent>AQAB</Exponent>
  <P>+hhxh9KjXvS...vWL47IlE=</P>
  <Q>x9IxFrOIpj...z3eGi4s=</Q>
  <DP>Dp2bFOr0...26/SWOE=</DP>
  <DQ>oAKxTHx3...zMbn+Tq9gw==</DQ>
  <InverseQ>RapfQxpRbPa...q80Vcl9Pc=</InverseQ>
  <D>A9q412ejcU8PL...WJ1xnKcUWzwQ==</D>
</RSAKeyValue>
```

PEM key:
```
-----BEGIN PRIVATE KEY-----
MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAwzZYQpFhIItfo5Z9
Ns7HHYWzqRel/OUK2XGyv8qBoZBWxd3DOiPbczbwDtOZDy8c0NejmEcbmk+3CZXg
AyOc+wIDAQABAkAD2rjXZ6NxTw8uXbRPNrn7vT4U4qCmY6dZL8OFwevZUsVhpsNM
wmH62l/5Le4zd66atsRohK1+hYnXGcpxRbPBAiEA+hhxh9KjXvS6x1SyB6C2QHrI
gVyxWmmIVJdvWL47IlECIQDH0jEWs4imMNzvcViwab9GsZVwt5x6hgoufILPd4aL
iwIgDp2bFOr0bTo0KC4E8Xks7Xu/d//oxXXhZ8Ap26/SWOECIACgArFMfHfwnTBO
jXV3zzZcZdhFasLjWnLMxuf5Or2DAiBFql9DGlFs9o3f/06UNlFAbQcERTnv13Q1
WrzRVyX09w==
-----END PRIVATE KEY-----
```

### How to use

First of all you need to install [JRE 8+ (Java Runtime Environment)](https://java.com/ru/download/).
After that download file from `dist` folder and run:
```
java -jar rsaxml2pem.jar <file>
```
where `<file>` - path to xml file for convert.

For example:
```
java -jar rsaxml2pem.jar /home/user/keys/rsa_2048_public_key.xml
```

### License

MIT