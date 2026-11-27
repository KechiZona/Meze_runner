.text
.globl main
main:
    li a7,5
    ecall
    mv s0,a0
    li t0,0
    li t1,32

loop:
    bge t0,t1,cixis
    sll s1,s0,t0
    srli s1,s1,31
    addi t0,t0,1

    li a7,1
    add a0,zero,s1
    ecall

    j loop

cixis:
    li a7,11
    li a0,10
    ecall

    li a7,10
    ecall