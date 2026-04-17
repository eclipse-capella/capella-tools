# ![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.pde.runtime/icons/obj16/package_obj@2x.png) behavior
Behaviour aims at defining the core concepts of behavioural model.

## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractBehavior*

A Behavior is a specification of how its context classifier changes state over time.
The concept of behavior is extended to own Parameter Sets.
[source: UML superstructure v2.2]

Dynamic response to an excitation of an engineering thing.
[source: INCOSE AP233 WG, "System Engineering Concepts - A semantic glossary and model"]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)

Known Sub Types: ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/StateMachine.gif) [StateMachine](https://github.com/eclipse/capella/wiki/Metamodel-capellacommon#-statemachine), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/Scenario.gif) [Scenario](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-scenario)

**Specific fields**
|Attribute|Type|Range|Description|
|--|--|--|--|
|isControlOperator|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EBoolean](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-eboolean)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#iscontroloperator "Tells whether the type of this behavior node is to be treated as control[source: UML superstructure v2.2]") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.jdt.ui/icons/full/obj16/annotation_obj.png)](# "semantic")


|Reference|Type|Range|Description|
|--|--|--|--|
|ownedParameter|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractParameter](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractparameter)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#ownedparameter "References a list of parameters to the behavior that describes the order and type of arguments that can be givenwhen the behavior is invoked and of the values that will be returned when the behavior completes its execution[source: UML superstructure v2.2]")  |
|ownedParameterSet|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractParameterSet](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractparameterset)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#ownedparameterset "The ParameterSets owned by this Behavior[source: UML superstructure v2.2]")  |


---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractSignal*

A signal is a specification of send request instances communicated between objects. The receiving object handles the
received request instances as specified by its receptions. The data carried by a send request (which was passed to it by the
send invocation occurrence that caused that request) are represented as attributes of the signal. A signal is defined
independently of the classifiers handling the signal occurrence
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)

Known Sub Types: ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ExchangeItem.gif) [ExchangeItem](https://github.com/eclipse/capella/wiki/Metamodel-information#-exchangeitem), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/Signal.gif) [Signal](https://github.com/eclipse/capella/wiki/Metamodel-communication#-signal)


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype))*

|Reference|Type|Range|Description|
|--|--|--|--|
|*abstractTypedElements*|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttypedelements "the list of typed elements that reference this type") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#abstracttypedelements "opposite(AbstractTypedElement.abstractType)") |

---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractEvent*

An event is the specification of some occurrence that may potentially trigger effects by an object
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)

Known Sub Types: ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ChangeEvent.gif) [ChangeEvent](https://github.com/eclipse/capella/wiki/Metamodel-capellacommon#-changeevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/TimeEvent.gif) [TimeEvent](https://github.com/eclipse/capella/wiki/Metamodel-capellacommon#-timeevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/SystemFunction.gif) [SystemFunction](https://github.com/eclipse/capella/wiki/Metamodel-ctx#-systemfunction), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/FunctionInputPort.gif) [FunctionInputPort](https://github.com/eclipse/capella/wiki/Metamodel-fa#-functioninputport), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/FunctionOutputPort.gif) [FunctionOutputPort](https://github.com/eclipse/capella/wiki/Metamodel-fa#-functionoutputport), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/FunctionalExchange.gif) [FunctionalExchange](https://github.com/eclipse/capella/wiki/Metamodel-fa#-functionalexchange), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ComponentExchange.gif) [ComponentExchange](https://github.com/eclipse/capella/wiki/Metamodel-fa#-componentexchange), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/Service.gif) [Service](https://github.com/eclipse/capella/wiki/Metamodel-information#-service), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ExchangeItem.gif) [ExchangeItem](https://github.com/eclipse/capella/wiki/Metamodel-information#-exchangeitem), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/CreationEvent.gif) [CreationEvent](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-creationevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/DestructionEvent.gif) [DestructionEvent](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-destructionevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ExecutionEvent.gif) [ExecutionEvent](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-executionevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/EventReceiptOperation.gif) [EventReceiptOperation](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-eventreceiptoperation), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/EventSentOperation.gif) [EventSentOperation](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-eventsentoperation), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/ArmTimerEvent.gif) [ArmTimerEvent](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-armtimerevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/CancelTimerEvent.gif) [CancelTimerEvent](https://github.com/eclipse/capella/wiki/Metamodel-interaction#-canceltimerevent), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/LogicalFunction.gif) [LogicalFunction](https://github.com/eclipse/capella/wiki/Metamodel-la#-logicalfunction), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/OperationalActivity.gif) [OperationalActivity](https://github.com/eclipse/capella/wiki/Metamodel-oa#-operationalactivity), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/CommunicationMean.gif) [CommunicationMean](https://github.com/eclipse/capella/wiki/Metamodel-oa#-communicationmean), ![](https://raw.githubusercontent.com/eclipse/capella/master/core/plugins/org.polarsys.capella.core.data.res.edit/icons/full/obj16/PhysicalFunction.gif) [PhysicalFunction](https://github.com/eclipse/capella/wiki/Metamodel-pa#-physicalfunction)


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype))*

|Reference|Type|Range|Description|
|--|--|--|--|
|*abstractTypedElements*|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttypedelements "the list of typed elements that reference this type") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#abstracttypedelements "opposite(AbstractTypedElement.abstractType)") |

---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractTimeEvent*

A time event specifies a point in time by an expression. The expression might be absolute or might be relative to some
other point in time.
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)



**Specific fields**
|Attribute|Type|Range|Description|
|--|--|--|--|
|isRelative|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EDataType.gif) [EBoolean](https://github.com/eclipse/capella/wiki/Metamodel-ecore#-eboolean)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#isrelative "Specifies whether it is relative or absolute time[source: UML superstructure v2.2]") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.jdt.ui/icons/full/obj16/annotation_obj.png)](# "semantic")


|Reference|Type|Range|Description|
|--|--|--|--|
|when|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [TimeExpression](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-timeexpression)|1..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#when "Specifies the corresponding time deadline[source: UML superstructure v2.2]")  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractEvent](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-abstractevent),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype))*

|Reference|Type|Range|Description|
|--|--|--|--|
|*abstractTypedElements*|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttypedelements "the list of typed elements that reference this type") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#abstracttypedelements "opposite(AbstractTypedElement.abstractType)") |

---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractMessageEvent*

A message event specifies the receipt by an object of either a call or a signal
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)




**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractEvent](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-abstractevent),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype))*

|Reference|Type|Range|Description|
|--|--|--|--|
|*abstractTypedElements*|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttypedelements "the list of typed elements that reference this type") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#abstracttypedelements "opposite(AbstractTypedElement.abstractType)") |

---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *AbstractSignalEvent*

A signal event represents the receipt of an asynchronous signal. A signal event may cause a response, such as a state
machine transition as specified in the classifier behavior of the classifier that specified the receiver object, if the signal
referenced by the send request is mentioned in a reception owned or inherited by the classifier that specified the receiver
object.
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)



**Specific fields**

|Reference|Type|Range|Description|
|--|--|--|--|
|signal|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractSignal](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-abstractsignal)|1..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#signal "The specific signal that is associated with this event[source: UML superstructure v2.2]")  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractMessageEvent](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-abstractmessageevent),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractEvent](https://github.com/eclipse/capella/wiki/Metamodel-behavior#-abstractevent),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype))*

|Reference|Type|Range|Description|
|--|--|--|--|
|*abstractTypedElements*|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement)|0..*|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttypedelements "the list of typed elements that reference this type") [![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.debug.ui/icons/full/obj16/read_obj.png)](#abstracttypedelements "opposite(AbstractTypedElement.abstractType)") |

---
## ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) *TimeExpression*

A Time Expression defines a value specification that represents a time value
[source: UML superstructure v2.2]

Common Types: ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ModelElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-modelelement)



**Specific fields**

|Reference|Type|Range|Description|
|--|--|--|--|
|expression|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ValueSpecification](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-valuespecification)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#expression "The value of the time expression[source: UML superstructure v2.2]")  |
|observations|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractNamedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstractnamedelement)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#observations "Refers to the time and duration observations that are involved in expr[source: UML superstructure v2.2]")  |


**Inherited fields** *(from ![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [ValueSpecification](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-valuespecification),![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractTypedElement](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttypedelement))*

|Reference|Type|Range|Description|
|--|--|--|--|
|abstractType|![](https://raw.githubusercontent.com/kchobantonov/org.eclipse.emf/master/plugins/org.eclipse.emf.ecore.edit/icons/full/obj16/EClass.gif) [AbstractType](https://github.com/eclipse/capella/wiki/Metamodel-modellingcore#-abstracttype)|0..1|[![](https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.images/master/org.eclipse.images/eclipse-png/org.eclipse.equinox.p2.ui.discovery/icons/obj16/message_info.png)](#abstracttype "the type associated to this model element")  |

---

