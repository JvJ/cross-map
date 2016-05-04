// Compiled by ClojureScript 1.7.228 {}
goog.provide('cross_map.util');
goog.require('cljs.core');
/**
 * Alias for core/partial.
 */
cross_map.util.$ = (function cross_map$util$$(var_args){
var args__19641__auto__ = [];
var len__19634__auto___20180 = arguments.length;
var i__19635__auto___20181 = (0);
while(true){
if((i__19635__auto___20181 < len__19634__auto___20180)){
args__19641__auto__.push((arguments[i__19635__auto___20181]));

var G__20182 = (i__19635__auto___20181 + (1));
i__19635__auto___20181 = G__20182;
continue;
} else {
}
break;
}

var argseq__19642__auto__ = ((((2) < args__19641__auto__.length))?(new cljs.core.IndexedSeq(args__19641__auto__.slice((2)),(0))):null);
return cross_map.util.$.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__19642__auto__);
});

cross_map.util.$.cljs$core$IFn$_invoke$arity$variadic = (function (_AMPERSAND_form,_AMPERSAND_env,body){
return cljs.core.sequence.call(null,cljs.core.seq.call(null,cljs.core.concat.call(null,cljs.core._conj.call(null,cljs.core.List.EMPTY,new cljs.core.Symbol("cljs.core","partial","cljs.core/partial",1483172485,null)),body)));
});

cross_map.util.$.cljs$lang$maxFixedArity = (2);

cross_map.util.$.cljs$lang$applyTo = (function (seq20177){
var G__20178 = cljs.core.first.call(null,seq20177);
var seq20177__$1 = cljs.core.next.call(null,seq20177);
var G__20179 = cljs.core.first.call(null,seq20177__$1);
var seq20177__$2 = cljs.core.next.call(null,seq20177__$1);
return cross_map.util.$.cljs$core$IFn$_invoke$arity$variadic(G__20178,G__20179,seq20177__$2);
});

cross_map.util.$.cljs$lang$macro = true;
/**
 * Composition.
 */
cross_map.util._LT__BAR_ = (function cross_map$util$_LT__BAR_(var_args){
var args__19641__auto__ = [];
var len__19634__auto___20186 = arguments.length;
var i__19635__auto___20187 = (0);
while(true){
if((i__19635__auto___20187 < len__19634__auto___20186)){
args__19641__auto__.push((arguments[i__19635__auto___20187]));

var G__20188 = (i__19635__auto___20187 + (1));
i__19635__auto___20187 = G__20188;
continue;
} else {
}
break;
}

var argseq__19642__auto__ = ((((2) < args__19641__auto__.length))?(new cljs.core.IndexedSeq(args__19641__auto__.slice((2)),(0))):null);
return cross_map.util._LT__BAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__19642__auto__);
});

cross_map.util._LT__BAR_.cljs$core$IFn$_invoke$arity$variadic = (function (_AMPERSAND_form,_AMPERSAND_env,body){
return cljs.core.sequence.call(null,cljs.core.seq.call(null,cljs.core.concat.call(null,cljs.core._conj.call(null,cljs.core.List.EMPTY,new cljs.core.Symbol("cljs.core","comp","cljs.core/comp",-2137591872,null)),body)));
});

cross_map.util._LT__BAR_.cljs$lang$maxFixedArity = (2);

cross_map.util._LT__BAR_.cljs$lang$applyTo = (function (seq20183){
var G__20184 = cljs.core.first.call(null,seq20183);
var seq20183__$1 = cljs.core.next.call(null,seq20183);
var G__20185 = cljs.core.first.call(null,seq20183__$1);
var seq20183__$2 = cljs.core.next.call(null,seq20183__$1);
return cross_map.util._LT__BAR_.cljs$core$IFn$_invoke$arity$variadic(G__20184,G__20185,seq20183__$2);
});

cross_map.util._LT__BAR_.cljs$lang$macro = true;
/**
 * Create an exception in clj or cljs.
 */
cross_map.util.Err = (function cross_map$util$Err(var_args){
var args__19641__auto__ = [];
var len__19634__auto___20192 = arguments.length;
var i__19635__auto___20193 = (0);
while(true){
if((i__19635__auto___20193 < len__19634__auto___20192)){
args__19641__auto__.push((arguments[i__19635__auto___20193]));

var G__20194 = (i__19635__auto___20193 + (1));
i__19635__auto___20193 = G__20194;
continue;
} else {
}
break;
}

var argseq__19642__auto__ = ((((2) < args__19641__auto__.length))?(new cljs.core.IndexedSeq(args__19641__auto__.slice((2)),(0))):null);
return cross_map.util.Err.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__19642__auto__);
});

cross_map.util.Err.cljs$core$IFn$_invoke$arity$variadic = (function (_AMPERSAND_form,_AMPERSAND_env,body){
return null;
});

cross_map.util.Err.cljs$lang$maxFixedArity = (2);

cross_map.util.Err.cljs$lang$applyTo = (function (seq20189){
var G__20190 = cljs.core.first.call(null,seq20189);
var seq20189__$1 = cljs.core.next.call(null,seq20189);
var G__20191 = cljs.core.first.call(null,seq20189__$1);
var seq20189__$2 = cljs.core.next.call(null,seq20189__$1);
return cross_map.util.Err.cljs$core$IFn$_invoke$arity$variadic(G__20190,G__20191,seq20189__$2);
});

cross_map.util.Err.cljs$lang$macro = true;
/**
 * Utility function that determines whether a value is
 *   a two-element vector.
 */
cross_map.util.pair_QMARK_ = (function cross_map$util$pair_QMARK_(v){
return (cljs.core.vector_QMARK_.call(null,v)) && (cljs.core._EQ_.call(null,cljs.core.count.call(null,v),(2)));
});
/**
 * Utility for cross-platform key-value-pair creation.
 */
cross_map.util.kvp = (function cross_map$util$kvp(k,v){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,v], null);
});
cross_map.util.vmemo = (function cross_map$util$vmemo(f){
var mem = cljs.core.volatile_BANG_.call(null,cljs.core.PersistentArrayMap.EMPTY);
return ((function (mem){
return (function() { 
var G__20195__delegate = function (args){
var or__18576__auto__ = cljs.core.deref.call(null,mem).call(null,args);
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
var ret = cljs.core.apply.call(null,f,args);
cljs.core._vreset_BANG_.call(null,mem,ret.call(null,cljs.core._deref.call(null,mem)));

return ret;
}
};
var G__20195 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__20196__i = 0, G__20196__a = new Array(arguments.length -  0);
while (G__20196__i < G__20196__a.length) {G__20196__a[G__20196__i] = arguments[G__20196__i + 0]; ++G__20196__i;}
  args = new cljs.core.IndexedSeq(G__20196__a,0);
} 
return G__20195__delegate.call(this,args);};
G__20195.cljs$lang$maxFixedArity = 0;
G__20195.cljs$lang$applyTo = (function (arglist__20197){
var args = cljs.core.seq(arglist__20197);
return G__20195__delegate(args);
});
G__20195.cljs$core$IFn$_invoke$arity$variadic = G__20195__delegate;
return G__20195;
})()
;
;})(mem))
});
/**
 * Dissociates an entry from a nested associative structure returning a new
 *   nested structure. keys is a sequence of keys. Any empty maps that result
 *   will not be present in the new structure.
 * 
 *   Source code copied from clojure.core.incubator, since that library is
 *   clj-specific, and this should be cross-platform.
 */
cross_map.util.dissoc_in = (function cross_map$util$dissoc_in(m,p__20198){
var vec__20200 = p__20198;
var k = cljs.core.nth.call(null,vec__20200,(0),null);
var ks = cljs.core.nthnext.call(null,vec__20200,(1));
var keys = vec__20200;
if(cljs.core.truth_(ks)){
var temp__4423__auto__ = cljs.core.get.call(null,m,k);
if(cljs.core.truth_(temp__4423__auto__)){
var nextmap = temp__4423__auto__;
var newmap = cross_map$util$dissoc_in.call(null,nextmap,ks);
if(cljs.core.seq.call(null,newmap)){
return cljs.core.assoc.call(null,m,k,newmap);
} else {
return cljs.core.dissoc.call(null,m,k);
}
} else {
return m;
}
} else {
return cljs.core.dissoc.call(null,m,k);
}
});
/**
 * Like assoc-in, but works only on transient maps.
 */
cross_map.util.assoc_in_BANG_ = (function cross_map$util$assoc_in_BANG_(m,p__20201,v){
var vec__20203 = p__20201;
var k = cljs.core.nth.call(null,vec__20203,(0),null);
var ks = cljs.core.nthnext.call(null,vec__20203,(1));
if(cljs.core.truth_(ks)){
return cljs.core.assoc_BANG_.call(null,m,k,cross_map$util$assoc_in_BANG_.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),ks,v));
} else {
return cljs.core.assoc_BANG_.call(null,m,k,v);
}
});
/**
 * Like update, but works only on tansient maps.
 */
cross_map.util.update_BANG_ = (function cross_map$util$update_BANG_(var_args){
var args20204 = [];
var len__19634__auto___20214 = arguments.length;
var i__19635__auto___20215 = (0);
while(true){
if((i__19635__auto___20215 < len__19634__auto___20214)){
args20204.push((arguments[i__19635__auto___20215]));

var G__20216 = (i__19635__auto___20215 + (1));
i__19635__auto___20215 = G__20216;
continue;
} else {
}
break;
}

var G__20213 = args20204.length;
switch (G__20213) {
case 3:
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
var argseq__19653__auto__ = (new cljs.core.IndexedSeq(args20204.slice((6)),(0)));
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]),argseq__19653__auto__);

}
});

cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (m,k,f){
return cljs.core.assoc_BANG_.call(null,m,k,f.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY))));
});

cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (m,k,f,x){
return cljs.core.assoc_BANG_.call(null,m,k,f.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),x));
});

cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$5 = (function (m,k,f,x,y){
return cljs.core.assoc_BANG_.call(null,m,k,f.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),x,y));
});

cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$6 = (function (m,k,f,x,y,z){
return cljs.core.assoc_BANG_.call(null,m,k,f.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),x,y,z));
});

cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (m,k,f,x,y,z,more){
return cljs.core.apply.call(null,cljs.core.assoc_BANG_,m,k,f.call(null,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY))),x,y,z,more);
});

cross_map.util.update_BANG_.cljs$lang$applyTo = (function (seq20205){
var G__20206 = cljs.core.first.call(null,seq20205);
var seq20205__$1 = cljs.core.next.call(null,seq20205);
var G__20207 = cljs.core.first.call(null,seq20205__$1);
var seq20205__$2 = cljs.core.next.call(null,seq20205__$1);
var G__20208 = cljs.core.first.call(null,seq20205__$2);
var seq20205__$3 = cljs.core.next.call(null,seq20205__$2);
var G__20209 = cljs.core.first.call(null,seq20205__$3);
var seq20205__$4 = cljs.core.next.call(null,seq20205__$3);
var G__20210 = cljs.core.first.call(null,seq20205__$4);
var seq20205__$5 = cljs.core.next.call(null,seq20205__$4);
var G__20211 = cljs.core.first.call(null,seq20205__$5);
var seq20205__$6 = cljs.core.next.call(null,seq20205__$5);
return cross_map.util.update_BANG_.cljs$core$IFn$_invoke$arity$variadic(G__20206,G__20207,G__20208,G__20209,G__20210,G__20211,seq20205__$6);
});

cross_map.util.update_BANG_.cljs$lang$maxFixedArity = (6);
/**
 * Like update-in, but works only on transient maps.
 */
cross_map.util.update_in_BANG_ = (function cross_map$util$update_in_BANG_(var_args){
var args__19641__auto__ = [];
var len__19634__auto___20224 = arguments.length;
var i__19635__auto___20225 = (0);
while(true){
if((i__19635__auto___20225 < len__19634__auto___20224)){
args__19641__auto__.push((arguments[i__19635__auto___20225]));

var G__20226 = (i__19635__auto___20225 + (1));
i__19635__auto___20225 = G__20226;
continue;
} else {
}
break;
}

var argseq__19642__auto__ = ((((3) < args__19641__auto__.length))?(new cljs.core.IndexedSeq(args__19641__auto__.slice((3)),(0))):null);
return cross_map.util.update_in_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__19642__auto__);
});

cross_map.util.update_in_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (m,p__20222,f,args){
var vec__20223 = p__20222;
var k = cljs.core.nth.call(null,vec__20223,(0),null);
var ks = cljs.core.nthnext.call(null,vec__20223,(1));
if(cljs.core.truth_(ks)){
return cljs.core.assoc_BANG_.call(null,m,k,cljs.core.apply.call(null,cross_map.util.update_in_BANG_,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),ks,f,args));
} else {
return cljs.core.assoc_BANG_.call(null,m,k,cljs.core.apply.call(null,f,cljs.core.get.call(null,m,k,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY)),args));
}
});

cross_map.util.update_in_BANG_.cljs$lang$maxFixedArity = (3);

cross_map.util.update_in_BANG_.cljs$lang$applyTo = (function (seq20218){
var G__20219 = cljs.core.first.call(null,seq20218);
var seq20218__$1 = cljs.core.next.call(null,seq20218);
var G__20220 = cljs.core.first.call(null,seq20218__$1);
var seq20218__$2 = cljs.core.next.call(null,seq20218__$1);
var G__20221 = cljs.core.first.call(null,seq20218__$2);
var seq20218__$3 = cljs.core.next.call(null,seq20218__$2);
return cross_map.util.update_in_BANG_.cljs$core$IFn$_invoke$arity$variadic(G__20219,G__20220,G__20221,seq20218__$3);
});
/**
 * Like dissoc-in, but works only on transient maps.
 */
cross_map.util.dissoc_in_BANG_ = (function cross_map$util$dissoc_in_BANG_(m,p__20227){
var vec__20229 = p__20227;
var k = cljs.core.nth.call(null,vec__20229,(0),null);
var ks = cljs.core.nthnext.call(null,vec__20229,(1));
var keys = vec__20229;
if(cljs.core.truth_(ks)){
var temp__4423__auto__ = cljs.core.get.call(null,m,k);
if(cljs.core.truth_(temp__4423__auto__)){
var nextmap = temp__4423__auto__;
var newmap = cross_map$util$dissoc_in_BANG_.call(null,nextmap,ks);
if((cljs.core.count.call(null,newmap) > (0))){
return cljs.core.assoc_BANG_.call(null,m,k,newmap);
} else {
return cljs.core.dissoc_BANG_.call(null,m,k);
}
} else {
return m;
}
} else {
return cljs.core.dissoc_BANG_.call(null,m,k);
}
});

//# sourceMappingURL=util.js.map?rel=1462308728291