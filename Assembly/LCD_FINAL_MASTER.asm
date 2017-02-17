



;A continuación se mostrará la arquitectura del programa.
;Implementando el lenguaje de programación Assembler para
;crear las funciones definidas durante la etapa de diseño. Antes
;de proceder a leer este programa, es recomendable que cuente
;con el set de instrucciones de la familia del 68HC08.

;PROGRAMA DE CONTROL DE DISPLAY LCD 16X2

;PROGRAMADOR:	SEBASTIAN ANDRADE
;EMPRESA:	ESCUELA TÉCNICA N°1 OTTO KRAUSE
;DIVISIÓN:	5° ELECTRONICA 2
;ASIGNATURA:	MICROCONTROLADORES

;--------------------------------------------------------------------
;ACTUALIZACIONES
;--------------------------------------------------------------------

;VERSION:	0.00
;			Testeo de inicialización del LCD						aprobado
;VERSION:	0.01
;			Testeo de muestreo de mensaje						desaprobado
;			Cambio del mensaje por medio de un pulsador		aprobado
;VERSION:	0.02
;			Testeo de muestreo de mensaje						desaprobado
;			Mejora en el aprovechamiento de la memoria		aprobado
;VERSION:	0.03
;			Depuración de errores en el mensaje				aprobado
;VERSION:	1.00
;			Muestreo de dos mensajes de 16x2
;VERSION:	1.01
;			Mejora en la sintaxis del programa
;			Agregado un bucle para el envío del ASCII al LCD
;			Muestreo de cuatro mensajes de 16x2
;VERSION:	1.02
;			Asignación de pull-ups internos
;			Mejora en el aprovechamiento de la memoria
;			Arreglo del problema de rebote que ocasiona el pulsador
;VERSION:	1.03
;			Perfeccionamiento de la respuesta ante el pulso
;			Agregado de un mensaje adicional
;VERSION:	1.04
;			Agregado un bucle para simplificar el envío del mensaje
;			Ahorro del espacio de memoria
;			Acotadas las instrucciones para modificar el mensaje
;			Versión final para entregar

;--------------------------------------------------------------------
;       Incluir archivo con asociacion entre etiqueta / nombre  y las direcciones
;       de los registros asociados al microcontrolador
;--------------------------------------------------------------------

$Include	'C:\pemicro\ics08qtqyz\qtqy_registers.inc'

;--------------------------------------------------------------------
;Definicion de origenes de las memorias a utilizar
;--------------------------------------------------------------------

RAMStart		EQU	$0080					;Define la memoria tipo RAM
FLASHROMStart       EQU	$EE00	;Define la memoria FLASHROM
VECTORStart		EQU	$FFFE		;Define el VECTOR de reinicio

;--------------------------------------------------------------------
;Definicion de Puertos (Entity)
;--------------------------------------------------------------------

E				EQU	3		;Define el pin de activación del LCD	(Salida)
RS			EQU	1		;Define el pin de registro de control/datos	(Salida)
PUL		EQU	4		;Define el pin del pulsador	(Entrada)	PULL-UP INTERNO


;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA RAM (VARIABLES)
;--------------------------------------------------------------------

		ORG	RAMStart

;--------------------------------------------------------------------
;Variables para bucles
;--------------------------------------------------------------------

VAR_F	db	1				;Variable para palabras - de $1 a $10 - !16 caracteres
RET		db	1				;Longitud del retardo
CONT_P		db	1		;Variable que cuenta el numero del mensaje enviado

;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA FLASHROM (ESQUELETO DEL PROGRAMA)
;--------------------------------------------------------------------

		ORG	FLASHROMStart

;--------------------------------------------------------------------
;Mensajes a mostrar en el LCD - Exclusivamente deben ser mensajes de 16 caracteres c/u
;--------------------------------------------------------------------

P_1		DB	'ESCUELA  TECNICA'
P_2		DB	'* OTTO  KRAUSE *'
P_3		DB	'5o ELECTRONICA 2'
P_4		DB	'-@--- 2010 ---@-'
P_5		DB	'Trabajo Practico'
P_6		DB	'Microcontrollers'
P_7		DB    ' Dedicado al:   '
P_8		DB    'Prof J. Siamatas'
P_9		DB	'  ELECTRONICS   '
P_10	DB	'    RULES!!!    '
P_11	DB	'Going to the USA'
P_12	DB	'Charlottesville!'
P_13	DB	' Programmed by: '
P_14	DB	' L. L. Chamorro '

CANT	DB	$07

;*********************************************************************************************************
;***	INSTRUCCIONES:																							***
;***	Se pueden seguir añadiendo mensajes en la ROM													***
;***  	Agregar mensajes con su respectiva llamada y aumentar CANT en 1 bit cada 2 renglones	***
;*********************************************************************************************************

;--------------------------------------------------------------------
;INICIALIZA EL PROGRAMA DEL MICROCONTROLADOR (Arquitecture)
;--------------------------------------------------------------------

INIT:	
	mov	#$01,config1				;Configuraciones iniciales - Desactiva el WatchDog
	mov	#%1010,DDRA			;Asignación de PORTA
	mov	#$FF,DDRB				;Asignación de PORTB

	mov     #%110101,PTAPUE	;Asignación de pull-ups internos a los pines activos

	clra
	clr	PORTA								;Limpia los controles del LCD
	clr	PORTB								;Limpia el bus de datos

INIT_LCD:		;INICIALIZA EL LCD
	jsr	DELAY_LONG
	bclr	RS,PORTA						;--------------CONTROL INPUT
	mov	#%111000,PORTB		;Function Set (38)
	jsr	ENABLE
	jsr	DELAY_INIT
	mov	#%1110,PORTB			;Display ON/OFF Control (0E)
	jsr	ENABLE
	jsr	DELAY_INIT
	mov	#%110,PORTB				;Entry Mode Set (06)
	jsr	ENABLE
	jsr	DELAY_INIT
	mov	#%10100,PORTB			;Cursor or display shift
	jsr	ENABLE
	jsr	DELAY_INIT

WRITE:		;ESCRIBE EL MENSAJE
	clrx												;limpia el registro X que es usado como variable de incremento
	lda	CANT
	sta	CONT_P							;Setea el contador de mensajes
BUCLE:
	jsr	DDRAM							;Envía el mensaje
	dbnz	CONT_P,BUCLE			;Decrementa. Si CONT_P no es igual a cero, vuelve a Bucle
	bra	WRITE								;Vuelve al bucle para enviar el próximo mensaje

;--------------------------------------------------------------------
;SUBRUTINAS
;--------------------------------------------------------------------

ENABLE:				;Pulso de Habilitacion o Enable
	bset	E,PORTA						;Flanco ascendente de E
	lda	#$15
	jsr	DELAY								;Delay
	bclr	E,PORTA							;Flanco descendente de E
	lda	#$30
	jsr	DELAY								;Delay
	rts

DDRAM:
	bclr	RS,PORTA						;--------------CONTROL INPUT
	mov	#%01,PORTB				;Display Clear (01)
	jsr	ENABLE
	jsr	DELAY_INIT
	mov	#%10,PORTB				;Home (02)
	jsr	ENABLE
	jsr	DELAY_INIT
	mov	#$80,PORTB					;Set the DDRAM address
	jsr	ENABLE
	jsr	DELAY_INIT
	jsr	DATA_IN							;Ingreso del 1er renglón
	bclr	RS,PORTA						;--------------CONTROL INPUT
	mov	#$C0,PORTB				;Set the DDRAM address
	jsr	ENABLE
	jsr	DELAY_INIT
	jsr	DATA_IN							;Ingreso del 2do renglón
	mov	#$00,PORTB					;Limpia el PORTB
	jsr	DELAY_LONG
	jsr	DELAY_LONG
	jsr	DELAY_LONG
	jsr	PULSER							;Condición de cambio de mensaje
	rts

DATA_IN:
	bset	RS,PORTA					;--------------DATA INPUT
	mov	#$00,VAR_F					;variable para limitar el fin de renglon
DATA_LOAD:
	lda     $EE00,X							;Carga la letra del espacio de memoria
	sta	PORTB								;Ingresa letra en ASCII
	jsr	ENABLE
	incx												;Incrementa el registro X para la proxima letra
	inc	VAR_F
	brclr	4,VAR_F,DATA_LOAD	;Cuando llega al ingreso de los 16 caracteres, prosigue
	rts

PULSER:			;Cambio de mensaje por flanco ascendente
	jsr	DELAY_LONG
	brclr	PUL,PORTA,PULSER				;Si está en nivel alto, prosigue
IF_PULSER:
	jsr	DELAY_LONG
	brset	PUL,PORTA,IF_PULSER		;Si está en nivel bajo, prosigue
	jsr	DELAY_LONG
	jsr	DELAY_LONG
	rts

DELAY_LONG:						;DELAY - 60ms
	lda	#$FF
	jsr	DELAY
	rts
DELAY_INIT:							;DELAY - 10ms
	lda	#$30
DELAY:
	sta	RET
DEL:											;DELAY - Bucle
	lda	#$FF								;Setea A
	dbnza	*									;Decrementa A
	dbnz	RET,DEL					;Bucle
	clr	RET								;Limpia RET
	rts											;Vuelve al programa
	
;--------------------------------------------------------------------
;INICIALIZA EL ESPACIO DE MEMORIA DE VECTOR (VECTOR DE REINICIO)
;--------------------------------------------------------------------

		ORG	VECTORStart

        dw	INIT
