$Include	'C:\pemicro\ics08qtqyz\qtqy_registers.inc'

;--------------------------------------------------------------------
;Definicion de origenes de las memorias a utilizar
;--------------------------------------------------------------------

RAMStart		EQU	$0080				;Define la memoria tipo RAM
FLASHROMStart           EQU	$EE00	;Define la memoria FLASHROM
VECTORStart		EQU	$FFFE		;Define el VECTOR de reinicio

;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA RAM (VARIABLES)
;--------------------------------------------------------------------

		ORG	RAMStart

;Variables para bucles

count			db	1				;para el delay
instantes		db    1
TIEMPO		db    1
luchito			db    1

;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA FLASHROM (ESQUELETO DEL PROGRAMA)
;--------------------------------------------------------------------

		ORG	FLASHROMStart

INICIO:
	mov		#$01,config1			;Configuraciones iniciales - Desactiva el WatchDog
	mov		#$FF,DDRA			;Asignación de PORTA
	mov		#$FF,DDRB			;Asignación de PORTB
	mov		#$00,PORTB			;Todas entradas
	mov		#$00,PORTA			;Todas entradas
	jsr     muchotiempito			;Delay inicial

RESET_TIME:
	mov		#$01,TIEMPO	;setea la variable tiempo a un valor para encontrar la frecuencia
	mov		#$0,instantes		;clear la variable instantes para iniciar la vuelta

LETRA_1:			;P_VERDE Y O_ROJO
;P_VERDE_1 y O_ROJO_4
	mov		#$00,PORTB
	mov		#$00,PORTA
	jsr	VERDE_ENTERO
	bset	4,PORTA
	bset	5,PORTB
	bset	4,PORTB
	jsr		delayinstante
;P_VERDE_2 y O_ROJO_3
	mov		#$00,PORTB
	mov		#$00,PORTA
	bset	3,PORTB
	bset	2,PORTB
	bset	5,PORTA
	bset	4,PORTB
	jsr		delayinstante
;P_VERDE_3 y O_ROJO_2
	jsr		delayinstante
;P_VERDE_4 y O_ROJO_1
	mov		#$00,PORTB
	mov		#$00,PORTA
	bset	2,PORTB
	bset	1,PORTA
	bset	4,PORTA
	bset	5,PORTB
	bset	4,PORTB
	
	jsr	ESPACIO
	
LETRA_2:		;E_VERDE Y H_ROJO
;E_VERDE_1 y H_ROJO_4
	mov		#$00,PORTB
	mov		#$00,PORTA
	jsr	VERDE_ENTERO
	jsr	ROJO_ENTERO
	jsr		delayinstante
;E_VERDE_2 y H_ROJO_3
	mov		#$00,PORTB
	mov		#$00,PORTA
	bset      3,PORTB
	bset      1,PORTA
	bset      0,PORTB
	bset		5,PORTA
	jsr		delayinstante
;E_VERDE_3 y H_ROJO_2
	jsr		delayinstante
;E_VERDE_4 y H_ROJO_1
	bclr	1,PORTA
	jsr	ROJO_ENTERO
	jsr		delayinstante

	jsr	ESPACIO

LETRA_3:		;P_VERDE Y C_ROJO
;P_VERDE_1 y C_ROJO_4
	mov		#$00,PORTB
	mov		#$00,PORTA
	jsr	VERDE_ENTERO
	bset	4,PORTB
	jsr		delayinstante
;P_VERDE_2 y C_ROJO_3
	mov		#$00,PORTB
	mov		#$00,PORTA
	bset	3,PORTB
	bset	2,PORTB
	bset	7,PORTB
	bset	4,PORTB
	jsr		delayinstante
;P_VERDE_3 y C_ROJO_2
	jsr		delayinstante
;P_VERDE_4 y C_ROJO_1
	mov		#$00,PORTB
	mov		#$00,PORTA
	bset	2,PORTB
	bset	1,PORTA
	jsr	ROJO_ENTERO
	jsr		delayinstante

	jsr	ESPACIO

LETRA_4:		;O_VERDE Y U_ROJO
;O_VERDE_1 y U_ROJO_4
	mov     #$00,PORTB
	mov     #$00,PORTA
	bset	0,PORTA
	bset	1,PORTB
	bset	0,PORTB
	jsr	ROJO_ENTERO
	jsr		delayinstante
;O_VERDE_2 y U_ROJO_3
	mov     #$00,PORTB
	mov     #$00,PORTA
	bset	1,PORTA
	bset	0,PORTB
	bset	4,PORTB
	jsr		delayinstante
;O_VERDE_3 y U_ROJO_2
	jsr		delayinstante
;O_VERDE_3 y U_ROJO_2
	mov     #$00,PORTB
	mov     #$00,PORTA
	bset	0,PORTA
	bset	1,PORTB
	bset	0,PORTB
	jsr	ROJO_ENTERO
	jsr		delayinstante

	jsr	ESPACIO

LETRA_5:		;L_ROJO
	mov     #$00,PORTB
	mov     #$00,PORTA
	bset	4,PORTB
	jsr		delayinstante
	jsr		delayinstante
	jsr		delayinstante
	jsr	ROJO_ENTERO
	jsr		delayinstante
	
COMPENSACIÓN:
	jsr		delaycompensa
	jmp	RESET_TIME

;----------------------------------------------------------------------
;SUBRUTINAS
;----------------------------------------------------------------------

ESPACIO:
	jsr	VERDE_ESPACIO
	jsr	ROJO_ESPACIO
	jsr             delayinstante
	jsr             delayinstante
	jsr             delayinstante


VERDE_ESPACIO:						
	bclr	3,PORTB
	bclr	2,PORTB
	bclr	1,PORTA
	bclr	0,PORTA
	bclr	1,PORTB
	bclr	0,PORTB
	rts

VERDE_ENTERO:
	bset	3,PORTB
	bset	2,PORTB
	bset	1,PORTA
	bset	0,PORTA
	bset	1,PORTB
	bset	0,PORTB
	rts

ROJO_ESPACIO:						
	bclr	7,PORTB
	bclr	6,PORTB
	bclr	5,PORTA
	bclr	4,PORTA
	bclr	5,PORTB
	bclr	4,PORTB
	rts

ROJO_ENTERO:
	bset	7,PORTB
	bset	6,PORTB
	bset	5,PORTA
	bset	4,PORTA
	bset	5,PORTB
	bset	4,PORTB
	rts

delayinstante:
	inc	instantes
	mov	#!2,luchito
pepo:
	lda	#$DA
	dbnza	*
	dbnz	luchito,pepo
	rts

delaycompensa:			; laralala esta subrutina tiene un retardo de 0.6 milisegundos
luchi:
	jsr	delayinstante
	lda	instantes ; lo carga en el acumulador
	cbeqa   #$32,actsal2   ;cuenta si pasaron los 50 instantes para reiniciar el muestreo
	bra     luchi
actsal2:
	rts

muchotiempito:
	inc      instantes
	clr      count      ; limpio count
	bset    5,TSC      ; apago timer
	mov     #$36,TSC   ; configura timer y prescaler /64
	lda     #$50    ;     carga la variable TIEMPO
	sta     TMODH      ; cargo el registro de comparacion TMODH
	sta     TMODL      ; cargo el registro de comparacion TMODL
	bclr     5,TSC      ; activo timer
correr1:
	brset   7,TSC,des1  ; pregunta si desboedo el timer
	jmp     correr1     ;
des1:
	bclr    7,TSC      ; limpia el Flag de desborde
	inc     count      ; incrementa cantidad de desbordes
	lda     count      ; lo carga en el acumulador
	cbeqa   #$1,actsal1  ; verifica y llego a 1 desborde
	bra     correr1
actsal1:
	rts

;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA DE VECTOR (VECTOR DE REINICIO)
;--------------------------------------------------------------------

		ORG	VECTORStart

        dw	inicio