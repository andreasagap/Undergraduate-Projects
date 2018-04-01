# Andreas Agapitos
# AEM : 2530 

IP = (2, 6, 3, 1, 4, 8, 5, 7)
IPInverse = (4, 1, 3, 5, 7, 2, 8, 6)

P4 = (2, 4, 3, 1)

P8 = (6, 3, 7, 4, 8, 5, 10, 9)
P10 = (3, 5, 2, 7, 4, 10, 1, 9, 8, 6)

S0 = [
    [1, 0, 3, 2],
    [3, 2, 1, 0],
    [0, 2, 1, 3],
    [3, 1, 3, 2]
]

S1 = [
    [0, 1, 2, 3],
    [2, 0, 1, 3],
    [3, 0, 1, 0],
    [2, 1, 0, 3]
]
E = (4, 1, 2, 3, 2, 3, 4, 1)


def permutation(perm, key):
    permutated_key = ""
    for i in perm:
        permutated_key += key[i - 1]

    return permutated_key


def GenerateK1(left, right):  # Generate K1
    left_key_rot = left[1:] + left[:1]
    right_key_rot = right[1:] + right[:1]
    key_rot = left_key_rot + right_key_rot
    return permutation(P8, key_rot)


def GenerateK2(left, right):  # Generate K2
    left_key_rot = left[3:] + left[:3]
    right_key_rot = right[3:] + right[:3]
    key_rot = left_key_rot + right_key_rot
    return permutation(P8, key_rot)


def F(right, subkey):
    expanded_cipher = permutation(E, right)
    xor = bin(int(expanded_cipher, 2) ^ int(subkey, 2))[2:].zfill(8)
    left_xor = xor[:4]
    right_xor = xor[4:]
    left_sbox = Sbox(left_xor, S0)
    right_sbox = Sbox(right_xor, S1)
    return permutation(P4, left_sbox + right_sbox)  # We make permutation with P4 table.


def Sbox(input, sbox):
    row = int(input[0] + input[3], 2)
    column = int(input[1] + input[2], 2)
    return bin(sbox[row][column])[2:].zfill(4)
    # We have 4-bit and with zfill() we fill with 4 zeros before the start so we have 8-bit.


def f(left, rigth, key):  # ((L XOR F(R,KEY)),R)
    left = int(left, 2) ^ int(F(rigth, key), 2)  # Symbol ^ is XOR
    return bin(left)[2:].zfill(4), rigth  # zfill() fill with 4 zeros before the start of left


def decrypt(key, ciphertext):  # Firstly K2, secondly K1
    p10key = permutation(P10, str(key))
    left = p10key[:5]
    right = p10key[5:]

    K1 = GenerateK1(left, right)
    K2 = GenerateK2(left, right)
    print("K1: " + K1)
    print("K2: " + K2)

    permutated_cipher = permutation(IP, str(ciphertext))
    print("IP: " + permutated_cipher)
    first_half = permutated_cipher[:4]
    second_half = permutated_cipher[4:]
    left, right = f(first_half, second_half, K2)
    print("SW: " + right + left)
    left, right = f(right, left, K1)  # switch left and right!
    plainText = permutation(IPInverse, left + right)
    print("PlainText: " + plainText)


def encrypt(key, plaintext):  # Firstly K1, secondly K2
    p10key = permutation(P10, str(key))
    left = p10key[:5]
    right = p10key[5:]

    K1 = GenerateK1(left, right)
    K2 = GenerateK2(left, right)
    print("K1: " + K1)
    print("K2: " + K2)

    permutated_cipher = permutation(IP, str(plaintext))
    print("IP: " + permutated_cipher)
    first_half = permutated_cipher[:4]
    second_half = permutated_cipher[4:]
    left, right = f(first_half, second_half, K1)
    print("SW: " + right + left)
    left, right = f(right, left, K2)  # switch left and right!
    cipherText = permutation(IPInverse, left + right)
    print("CipherText: " + cipherText)


def main():
    option = input("Please enter E for encrypt or D for decrypt: ")
    while True:
        key = input("Please enter key with size 10-bit: ")
        if len(str(key)) == 10:
            break

    if str(option) == "E":
        while True:
            text = input("Please enter the plaintext 8-bit: ")
            if len(str(text)) == 8:
                break
        encrypt(key, text)
    elif str(option) == "D":
        while True:
            text = input("Please enter the cipherText 8-bit: ")
            if len(str(text)) == 8:
                break
        decrypt(key, text)


if __name__ == "__main__":
    main()
