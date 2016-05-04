// Compiled by ClojureScript 1.7.228 {}
goog.provide('cross_map.core');
goog.require('cljs.core');
goog.require('cross_map.util');

/**
 * This protocol must be implemented by all cross-maps.
 * @interface
 */
cross_map.core.ICrossMap = function(){};

/**
 * A map representing a single row.  A map of {col->element}.
 */
cross_map.core.row = (function cross_map$core$row(this$,r_key){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$row$arity$2 == null)))){
return this$.cross_map$core$ICrossMap$row$arity$2(this$,r_key);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.row[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$,r_key);
} else {
var m__19232__auto____$1 = (cross_map.core.row["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$,r_key);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.row",this$);
}
}
}
});

/**
 * A map representing a single column.  A map of {row->element}.
 */
cross_map.core.col = (function cross_map$core$col(this$,c_key){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$col$arity$2 == null)))){
return this$.cross_map$core$ICrossMap$col$arity$2(this$,c_key);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.col[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$,c_key);
} else {
var m__19232__auto____$1 = (cross_map.core.col["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$,c_key);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.col",this$);
}
}
}
});

/**
 * A map representing all rows.  A map of {row->{col->element}}.
 */
cross_map.core.rows = (function cross_map$core$rows(this$){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$rows$arity$1 == null)))){
return this$.cross_map$core$ICrossMap$rows$arity$1(this$);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.rows[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$);
} else {
var m__19232__auto____$1 = (cross_map.core.rows["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.rows",this$);
}
}
}
});

/**
 * A map representing all colums.  A map of {col->{row->element}}
 */
cross_map.core.cols = (function cross_map$core$cols(this$){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$cols$arity$1 == null)))){
return this$.cross_map$core$ICrossMap$cols$arity$1(this$);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.cols[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$);
} else {
var m__19232__auto____$1 = (cross_map.core.cols["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.cols",this$);
}
}
}
});

/**
 * Full-map cross-section by row-first.
 */
cross_map.core.crossIndexRows = (function cross_map$core$crossIndexRows(this$,r_keys,opts){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$crossIndexRows$arity$3 == null)))){
return this$.cross_map$core$ICrossMap$crossIndexRows$arity$3(this$,r_keys,opts);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.crossIndexRows[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$,r_keys,opts);
} else {
var m__19232__auto____$1 = (cross_map.core.crossIndexRows["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$,r_keys,opts);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.crossIndexRows",this$);
}
}
}
});

/**
 * Full-map cross-section by col-first.
 */
cross_map.core.crossIndexCols = (function cross_map$core$crossIndexCols(this$,c_keys,opts){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$crossIndexCols$arity$3 == null)))){
return this$.cross_map$core$ICrossMap$crossIndexCols$arity$3(this$,c_keys,opts);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.crossIndexCols[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$,c_keys,opts);
} else {
var m__19232__auto____$1 = (cross_map.core.crossIndexCols["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$,c_keys,opts);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.crossIndexCols",this$);
}
}
}
});

/**
 * Cross section of elements in all specified rows and all specified columns.
 */
cross_map.core.crossIndex = (function cross_map$core$crossIndex(this$,r_keys,c_keys,opts){
if((!((this$ == null))) && (!((this$.cross_map$core$ICrossMap$crossIndex$arity$4 == null)))){
return this$.cross_map$core$ICrossMap$crossIndex$arity$4(this$,r_keys,c_keys,opts);
} else {
var x__19231__auto__ = (((this$ == null))?null:this$);
var m__19232__auto__ = (cross_map.core.crossIndex[goog.typeOf(x__19231__auto__)]);
if(!((m__19232__auto__ == null))){
return m__19232__auto__.call(null,this$,r_keys,c_keys,opts);
} else {
var m__19232__auto____$1 = (cross_map.core.crossIndex["_"]);
if(!((m__19232__auto____$1 == null))){
return m__19232__auto____$1.call(null,this$,r_keys,c_keys,opts);
} else {
throw cljs.core.missing_protocol.call(null,"ICrossMap.crossIndex",this$);
}
}
}
});

/**
 * crossIndexRows and crossIndexColumns are the same if you switch
 *   around instances of rowIdx and colIdx.  Pass in true to rows? to
 *   iterate over rows.  False to iterate over columns.
 * 
 *   Explanation of the keyword params:
 *   (TODO)
 */
cross_map.core.cross_rows_cols_helper = (function cross_map$core$cross_rows_cols_helper(this$,rows_QMARK_,selected_keys,p__20233){
var map__20259 = p__20233;
var map__20259__$1 = ((((!((map__20259 == null)))?((((map__20259.cljs$lang$protocol_mask$partition0$ & (64))) || (map__20259.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__20259):map__20259);
var opts = map__20259__$1;
var every = cljs.core.get.call(null,map__20259__$1,new cljs.core.Keyword(null,"every","every",-2060295878));
var any = cljs.core.get.call(null,map__20259__$1,new cljs.core.Keyword(null,"any","any",1705907423));
var keys_only = cljs.core.get.call(null,map__20259__$1,new cljs.core.Keyword(null,"keys-only","keys-only",2032273394));
var vals_only = cljs.core.get.call(null,map__20259__$1,new cljs.core.Keyword(null,"vals-only","vals-only",-69943712));
var _ = (function (){var and__18564__auto__ = any;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = every;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception((cljs.core.truth_(rows_QMARK_)?":any-row and :every-row cannot both be specified.":":any-col and :every-col cannot both be specified.")));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$1 = cljs.core.disj.call(null,opts,new cljs.core.Keyword(null,"any","any",1705907423),new cljs.core.Keyword(null,"every","every",-2060295878));
var ___$1 = (function (){var and__18564__auto__ = keys_only;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = vals_only;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception([cljs.core.str("Invalid key/value options: "),cljs.core.str(opts__$1)].join('')));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$2 = cljs.core.disj.call(null,opts__$1,new cljs.core.Keyword(null,"keys-only","keys-only",2032273394),new cljs.core.Keyword(null,"vals-only","vals-only",-69943712));
var ___$2 = ((!(cljs.core.empty_QMARK_.call(null,opts__$2)))?(function(){throw (new java.lang.Exception([cljs.core.str("Unsupported options: "),cljs.core.str(opts__$2)].join('')))})():null);
var rowIdx = this$.rowIdx();
var colIdx = this$.colIdx();
var vec__20261 = (cljs.core.truth_(rows_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rowIdx,colIdx], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [colIdx,rowIdx], null));
var pri = cljs.core.nth.call(null,vec__20261,(0),null);
var sec = cljs.core.nth.call(null,vec__20261,(1),null);
var no_keys = cljs.core.empty_QMARK_.call(null,selected_keys);
var every__$1 = (function (){var and__18564__auto__ = !(no_keys);
if(and__18564__auto__){
var or__18576__auto__ = every;
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return cljs.core.not.call(null,any);
}
} else {
return and__18564__auto__;
}
})();
var selected_keys__$1 = (function (){var or__18576__auto__ = cljs.core.seq.call(null,selected_keys);
if(or__18576__auto__){
return or__18576__auto__;
} else {
return cljs.core.keys.call(null,pri);
}
})();
var kv_mode = (function (){var or__18576__auto__ = keys_only;
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return vals_only;
}
})();
var valid_QMARK_ = (cljs.core.truth_(every__$1)?cljs.core.every_QMARK_:cljs.core.some
);
var ps_keys = (cljs.core.truth_(every__$1)?(function (){var min_pk = cljs.core.apply.call(null,cljs.core.min_key,((function (_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function (p1__20232_SHARP_){
return cljs.core.count.call(null,pri.call(null,p1__20232_SHARP_));
});})(_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
,(function (){var or__18576__auto__ = cljs.core.seq.call(null,selected_keys__$1);
if(or__18576__auto__){
return or__18576__auto__;
} else {
return cljs.core.keys.call(null,pri);
}
})());
var iter__19348__auto__ = ((function (min_pk,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function cross_map$core$cross_rows_cols_helper_$_iter__20262(s__20263){
return (new cljs.core.LazySeq(null,((function (min_pk,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function (){
var s__20263__$1 = s__20263;
while(true){
var temp__4425__auto__ = cljs.core.seq.call(null,s__20263__$1);
if(temp__4425__auto__){
var s__20263__$2 = temp__4425__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__20263__$2)){
var c__19346__auto__ = cljs.core.chunk_first.call(null,s__20263__$2);
var size__19347__auto__ = cljs.core.count.call(null,c__19346__auto__);
var b__20265 = cljs.core.chunk_buffer.call(null,size__19347__auto__);
if((function (){var i__20264 = (0);
while(true){
if((i__20264 < size__19347__auto__)){
var sk = cljs.core._nth.call(null,c__19346__auto__,i__20264);
cljs.core.chunk_append.call(null,b__20265,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [min_pk,sk], null));

var G__20284 = (i__20264 + (1));
i__20264 = G__20284;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20265),cross_map$core$cross_rows_cols_helper_$_iter__20262.call(null,cljs.core.chunk_rest.call(null,s__20263__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20265),null);
}
} else {
var sk = cljs.core.first.call(null,s__20263__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [min_pk,sk], null),cross_map$core$cross_rows_cols_helper_$_iter__20262.call(null,cljs.core.rest.call(null,s__20263__$2)));
}
} else {
return null;
}
break;
}
});})(min_pk,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
,null,null));
});})(min_pk,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
;
return iter__19348__auto__.call(null,cljs.core.keys.call(null,pri.call(null,min_pk)));
})():(function (){var iter__19348__auto__ = ((function (_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function cross_map$core$cross_rows_cols_helper_$_iter__20266(s__20267){
return (new cljs.core.LazySeq(null,((function (_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function (){
var s__20267__$1 = s__20267;
while(true){
var temp__4425__auto__ = cljs.core.seq.call(null,s__20267__$1);
if(temp__4425__auto__){
var xs__4977__auto__ = temp__4425__auto__;
var pk = cljs.core.first.call(null,xs__4977__auto__);
var iterys__19344__auto__ = ((function (s__20267__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function cross_map$core$cross_rows_cols_helper_$_iter__20266_$_iter__20268(s__20269){
return (new cljs.core.LazySeq(null,((function (s__20267__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function (){
var s__20269__$1 = s__20269;
while(true){
var temp__4425__auto____$1 = cljs.core.seq.call(null,s__20269__$1);
if(temp__4425__auto____$1){
var s__20269__$2 = temp__4425__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,s__20269__$2)){
var c__19346__auto__ = cljs.core.chunk_first.call(null,s__20269__$2);
var size__19347__auto__ = cljs.core.count.call(null,c__19346__auto__);
var b__20271 = cljs.core.chunk_buffer.call(null,size__19347__auto__);
if((function (){var i__20270 = (0);
while(true){
if((i__20270 < size__19347__auto__)){
var sk = cljs.core._nth.call(null,c__19346__auto__,i__20270);
cljs.core.chunk_append.call(null,b__20271,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [pk,sk], null));

var G__20285 = (i__20270 + (1));
i__20270 = G__20285;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20271),cross_map$core$cross_rows_cols_helper_$_iter__20266_$_iter__20268.call(null,cljs.core.chunk_rest.call(null,s__20269__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20271),null);
}
} else {
var sk = cljs.core.first.call(null,s__20269__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [pk,sk], null),cross_map$core$cross_rows_cols_helper_$_iter__20266_$_iter__20268.call(null,cljs.core.rest.call(null,s__20269__$2)));
}
} else {
return null;
}
break;
}
});})(s__20267__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
,null,null));
});})(s__20267__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
;
var fs__19345__auto__ = cljs.core.seq.call(null,iterys__19344__auto__.call(null,cljs.core.keys.call(null,pri.call(null,pk))));
if(fs__19345__auto__){
return cljs.core.concat.call(null,fs__19345__auto__,cross_map$core$cross_rows_cols_helper_$_iter__20266.call(null,cljs.core.rest.call(null,s__20267__$1)));
} else {
var G__20286 = cljs.core.rest.call(null,s__20267__$1);
s__20267__$1 = G__20286;
continue;
}
} else {
return null;
}
break;
}
});})(_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
,null,null));
});})(_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
;
return iter__19348__auto__.call(null,selected_keys__$1);
})()
);
var tags_v = ((cljs.core.not.call(null,every__$1))?cljs.core.volatile_BANG_.call(null,cljs.core.transient$.call(null,cljs.core.PersistentHashSet.EMPTY)):null);
var iter__19348__auto__ = ((function (_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,ps_keys,tags_v,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function cross_map$core$cross_rows_cols_helper_$_iter__20272(s__20273){
return (new cljs.core.LazySeq(null,((function (_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,ps_keys,tags_v,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only){
return (function (){
var s__20273__$1 = s__20273;
while(true){
var temp__4425__auto__ = cljs.core.seq.call(null,s__20273__$1);
if(temp__4425__auto__){
var s__20273__$2 = temp__4425__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__20273__$2)){
var c__19346__auto__ = cljs.core.chunk_first.call(null,s__20273__$2);
var size__19347__auto__ = cljs.core.count.call(null,c__19346__auto__);
var b__20275 = cljs.core.chunk_buffer.call(null,size__19347__auto__);
if((function (){var i__20274 = (0);
while(true){
if((i__20274 < size__19347__auto__)){
var vec__20280 = cljs.core._nth.call(null,c__19346__auto__,i__20274);
var pk = cljs.core.nth.call(null,vec__20280,(0),null);
var sk = cljs.core.nth.call(null,vec__20280,(1),null);
if(cljs.core.truth_((function (){var and__18564__auto__ = (function (){var or__18576__auto__ = (function (){var and__18564__auto__ = tags_v;
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.not.call(null,cljs.core.deref.call(null,tags_v).call(null,sk));
} else {
return and__18564__auto__;
}
})();
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return every__$1;
}
})();
if(cljs.core.truth_(and__18564__auto__)){
return valid_QMARK_.call(null,sec.call(null,sk),selected_keys__$1);
} else {
return and__18564__auto__;
}
})())){
var ___$3 = (function (){var and__18564__auto__ = tags_v;
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core._vreset_BANG_.call(null,tags_v,cljs.core.conj_BANG_.call(null,cljs.core._deref.call(null,tags_v),sk));
} else {
return and__18564__auto__;
}
})();
cljs.core.chunk_append.call(null,b__20275,(function (){var G__20281 = (((kv_mode instanceof cljs.core.Keyword))?kv_mode.fqn:null);
switch (G__20281) {
case "keys-only":
return sk;

break;
case "vals-only":
return sec.call(null,sk);

break;
default:
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sk,sec.call(null,sk)], null);

}
})());

var G__20288 = (i__20274 + (1));
i__20274 = G__20288;
continue;
} else {
var G__20289 = (i__20274 + (1));
i__20274 = G__20289;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20275),cross_map$core$cross_rows_cols_helper_$_iter__20272.call(null,cljs.core.chunk_rest.call(null,s__20273__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20275),null);
}
} else {
var vec__20282 = cljs.core.first.call(null,s__20273__$2);
var pk = cljs.core.nth.call(null,vec__20282,(0),null);
var sk = cljs.core.nth.call(null,vec__20282,(1),null);
if(cljs.core.truth_((function (){var and__18564__auto__ = (function (){var or__18576__auto__ = (function (){var and__18564__auto__ = tags_v;
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.not.call(null,cljs.core.deref.call(null,tags_v).call(null,sk));
} else {
return and__18564__auto__;
}
})();
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return every__$1;
}
})();
if(cljs.core.truth_(and__18564__auto__)){
return valid_QMARK_.call(null,sec.call(null,sk),selected_keys__$1);
} else {
return and__18564__auto__;
}
})())){
var ___$3 = (function (){var and__18564__auto__ = tags_v;
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core._vreset_BANG_.call(null,tags_v,cljs.core.conj_BANG_.call(null,cljs.core._deref.call(null,tags_v),sk));
} else {
return and__18564__auto__;
}
})();
return cljs.core.cons.call(null,(function (){var G__20283 = (((kv_mode instanceof cljs.core.Keyword))?kv_mode.fqn:null);
switch (G__20283) {
case "keys-only":
return sk;

break;
case "vals-only":
return sec.call(null,sk);

break;
default:
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sk,sec.call(null,sk)], null);

}
})(),cross_map$core$cross_rows_cols_helper_$_iter__20272.call(null,cljs.core.rest.call(null,s__20273__$2)));
} else {
var G__20291 = cljs.core.rest.call(null,s__20273__$2);
s__20273__$1 = G__20291;
continue;
}
}
} else {
return null;
}
break;
}
});})(_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,ps_keys,tags_v,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
,null,null));
});})(_,opts__$1,___$1,opts__$2,___$2,rowIdx,colIdx,vec__20261,pri,sec,no_keys,every__$1,selected_keys__$1,kv_mode,valid_QMARK_,ps_keys,tags_v,map__20259,map__20259__$1,opts,every,any,keys_only,vals_only))
;
return iter__19348__auto__.call(null,ps_keys);
});
/**
 * Helper functoin for full cross indexing.
 */
cross_map.core.cross_index_helper = (function cross_map$core$cross_index_helper(this$,r_keys,c_keys,p__20300){
var map__20312 = p__20300;
var map__20312__$1 = ((((!((map__20312 == null)))?((((map__20312.cljs$lang$protocol_mask$partition0$ & (64))) || (map__20312.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__20312):map__20312);
var opts = map__20312__$1;
var any_row = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"any-row","any-row",-1993412064));
var every_row = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"every-row","every-row",1745758568));
var any_col = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"any-col","any-col",-1251521440));
var every_col = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"every-col","every-col",304177765));
var keys_only = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"keys-only","keys-only",2032273394));
var vals_only = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"vals-only","vals-only",-69943712));
var by_rows = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"by-rows","by-rows",21963973));
var by_cols = cljs.core.get.call(null,map__20312__$1,new cljs.core.Keyword(null,"by-cols","by-cols",376181199));
var _ = (function (){var and__18564__auto__ = every_row;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = any_row;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception("Cannot specify both :any-row and :every-row."));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$1 = cljs.core.disj.call(null,opts,new cljs.core.Keyword(null,"any-row","any-row",-1993412064),new cljs.core.Keyword(null,"every-row","every-row",1745758568));
var ___$1 = (function (){var and__18564__auto__ = every_col;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = any_col;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception("Cannot specify both :any-col and :every-col."));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$2 = cljs.core.disj.call(null,opts__$1,new cljs.core.Keyword(null,"any-col","any-col",-1251521440),new cljs.core.Keyword(null,"every-col","every-col",304177765));
var ___$2 = (function (){var and__18564__auto__ = keys_only;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = vals_only;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception("Cannot specify both :keys-only and :vals-only."));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$3 = cljs.core.disj.call(null,opts__$2,new cljs.core.Keyword(null,"keys-only","keys-only",2032273394),new cljs.core.Keyword(null,"vals-only","vals-only",-69943712));
var ___$3 = (function (){var and__18564__auto__ = by_rows;
if(cljs.core.truth_(and__18564__auto__)){
var and__18564__auto____$1 = by_cols;
if(cljs.core.truth_(and__18564__auto____$1)){
throw (new java.lang.Exception("Cannot specify both :by-rows and :by-cols."));
} else {
return and__18564__auto____$1;
}
} else {
return and__18564__auto__;
}
})();
var opts__$4 = cljs.core.disj.call(null,opts__$3,new cljs.core.Keyword(null,"by-rows","by-rows",21963973),new cljs.core.Keyword(null,"by-cols","by-cols",376181199));
var ___$4 = ((!(cljs.core.empty_QMARK_.call(null,opts__$4)))?(function(){throw (new java.lang.Exception([cljs.core.str("Unsupported options: "),cljs.core.str(opts__$4)].join('')))})():null);
var checkfn = ((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (f,coll){
var v = cljs.core.volatile_BANG_.call(null,cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY));
return ((function (v,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (k){
var or__18576__auto__ = cljs.core.deref.call(null,v).call(null,k);
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
var ret = f.call(null,k,coll);
cljs.core._vreset_BANG_.call(null,v,cljs.core.assoc_BANG_.call(null,cljs.core._deref.call(null,v),k,ret));

return ret;
}
});
;})(v,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
;
var by_rows__$1 = (function (){var or__18576__auto__ = by_rows;
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return cljs.core.not.call(null,by_cols);
}
})();
var vec__20314 = (cljs.core.truth_(by_rows__$1)?new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$.rowIdx(),every_row,any_row,r_keys,this$.colIdx(),every_col,any_col,c_keys], null):new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$.colIdx(),every_col,any_col,c_keys,this$.rowIdx(),every_row,any_row,r_keys], null));
var pri = cljs.core.nth.call(null,vec__20314,(0),null);
var every_pri = cljs.core.nth.call(null,vec__20314,(1),null);
var any_pri = cljs.core.nth.call(null,vec__20314,(2),null);
var pri_keys = cljs.core.nth.call(null,vec__20314,(3),null);
var sec = cljs.core.nth.call(null,vec__20314,(4),null);
var every_sec = cljs.core.nth.call(null,vec__20314,(5),null);
var any_sec = cljs.core.nth.call(null,vec__20314,(6),null);
var sec_keys = cljs.core.nth.call(null,vec__20314,(7),null);
var vec__20315 = (cljs.core.truth_((function (){var and__18564__auto__ = cljs.core.empty_QMARK_.call(null,pri_keys);
if(and__18564__auto__){
var or__18576__auto__ = every_pri;
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return cljs.core.not.call(null,any_pri);
}
} else {
return and__18564__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [false,cljs.core.keys.call(null,pri)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [every_pri,pri_keys], null));
var every_pri__$1 = cljs.core.nth.call(null,vec__20315,(0),null);
var pri_keys__$1 = cljs.core.nth.call(null,vec__20315,(1),null);
var vec__20316 = (cljs.core.truth_((function (){var and__18564__auto__ = cljs.core.empty_QMARK_.call(null,sec_keys);
if(and__18564__auto__){
var or__18576__auto__ = every_sec;
if(cljs.core.truth_(or__18576__auto__)){
return or__18576__auto__;
} else {
return cljs.core.not.call(null,any_sec);
}
} else {
return and__18564__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [false,cljs.core.keys.call(null,sec)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [every_sec,sec_keys], null));
var every_sec__$1 = cljs.core.nth.call(null,vec__20316,(0),null);
var sec_keys__$1 = cljs.core.nth.call(null,vec__20316,(1),null);
var valid_pri_QMARK_ = checkfn.call(null,(cljs.core.truth_(every_sec__$1)?((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (p1__20292_SHARP_,p2__20293_SHARP_){
var and__18564__auto__ = pri.call(null,p1__20292_SHARP_);
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.every_QMARK_.call(null,pri.call(null,p1__20292_SHARP_),p2__20293_SHARP_);
} else {
return and__18564__auto__;
}
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
:((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (p1__20294_SHARP_,p2__20295_SHARP_){
var and__18564__auto__ = pri.call(null,p1__20294_SHARP_);
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.some.call(null,pri.call(null,p1__20294_SHARP_),p2__20295_SHARP_);
} else {
return and__18564__auto__;
}
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
),sec_keys__$1);
var valid_sec_QMARK_ = checkfn.call(null,(cljs.core.truth_(every_pri__$1)?((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (p1__20296_SHARP_,p2__20297_SHARP_){
var and__18564__auto__ = sec.call(null,p1__20296_SHARP_);
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.every_QMARK_.call(null,sec.call(null,p1__20296_SHARP_),p2__20297_SHARP_);
} else {
return and__18564__auto__;
}
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
:((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (p1__20298_SHARP_,p2__20299_SHARP_){
var and__18564__auto__ = sec.call(null,p1__20298_SHARP_);
if(cljs.core.truth_(and__18564__auto__)){
return cljs.core.some.call(null,sec.call(null,p1__20298_SHARP_),p2__20299_SHARP_);
} else {
return and__18564__auto__;
}
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
),pri_keys__$1);
var iter__19348__auto__ = ((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function cross_map$core$cross_index_helper_$_iter__20317(s__20318){
return (new cljs.core.LazySeq(null,((function (_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (){
var s__20318__$1 = s__20318;
while(true){
var temp__4425__auto__ = cljs.core.seq.call(null,s__20318__$1);
if(temp__4425__auto__){
var xs__4977__auto__ = temp__4425__auto__;
var pk = cljs.core.first.call(null,xs__4977__auto__);
if(cljs.core.truth_(valid_pri_QMARK_.call(null,pk))){
var iterys__19344__auto__ = ((function (s__20318__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function cross_map$core$cross_index_helper_$_iter__20317_$_iter__20319(s__20320){
return (new cljs.core.LazySeq(null,((function (s__20318__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols){
return (function (){
var s__20320__$1 = s__20320;
while(true){
var temp__4425__auto____$1 = cljs.core.seq.call(null,s__20320__$1);
if(temp__4425__auto____$1){
var s__20320__$2 = temp__4425__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,s__20320__$2)){
var c__19346__auto__ = cljs.core.chunk_first.call(null,s__20320__$2);
var size__19347__auto__ = cljs.core.count.call(null,c__19346__auto__);
var b__20322 = cljs.core.chunk_buffer.call(null,size__19347__auto__);
if((function (){var i__20321 = (0);
while(true){
if((i__20321 < size__19347__auto__)){
var sk = cljs.core._nth.call(null,c__19346__auto__,i__20321);
var entry = cljs.core.find.call(null,this$.mainMap(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [pk,sk], null));
if(cljs.core.truth_((function (){var and__18564__auto__ = entry;
if(cljs.core.truth_(and__18564__auto__)){
return valid_sec_QMARK_.call(null,sk);
} else {
return and__18564__auto__;
}
})())){
cljs.core.chunk_append.call(null,b__20322,entry);

var G__20323 = (i__20321 + (1));
i__20321 = G__20323;
continue;
} else {
var G__20324 = (i__20321 + (1));
i__20321 = G__20324;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20322),cross_map$core$cross_index_helper_$_iter__20317_$_iter__20319.call(null,cljs.core.chunk_rest.call(null,s__20320__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__20322),null);
}
} else {
var sk = cljs.core.first.call(null,s__20320__$2);
var entry = cljs.core.find.call(null,this$.mainMap(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [pk,sk], null));
if(cljs.core.truth_((function (){var and__18564__auto__ = entry;
if(cljs.core.truth_(and__18564__auto__)){
return valid_sec_QMARK_.call(null,sk);
} else {
return and__18564__auto__;
}
})())){
return cljs.core.cons.call(null,entry,cross_map$core$cross_index_helper_$_iter__20317_$_iter__20319.call(null,cljs.core.rest.call(null,s__20320__$2)));
} else {
var G__20325 = cljs.core.rest.call(null,s__20320__$2);
s__20320__$1 = G__20325;
continue;
}
}
} else {
return null;
}
break;
}
});})(s__20318__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
,null,null));
});})(s__20318__$1,pk,xs__4977__auto__,temp__4425__auto__,_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
;
var fs__19345__auto__ = cljs.core.seq.call(null,iterys__19344__auto__.call(null,sec_keys__$1));
if(fs__19345__auto__){
return cljs.core.concat.call(null,fs__19345__auto__,cross_map$core$cross_index_helper_$_iter__20317.call(null,cljs.core.rest.call(null,s__20318__$1)));
} else {
var G__20326 = cljs.core.rest.call(null,s__20318__$1);
s__20318__$1 = G__20326;
continue;
}
} else {
var G__20327 = cljs.core.rest.call(null,s__20318__$1);
s__20318__$1 = G__20327;
continue;
}
} else {
return null;
}
break;
}
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
,null,null));
});})(_,opts__$1,___$1,opts__$2,___$2,opts__$3,___$3,opts__$4,___$4,checkfn,by_rows__$1,vec__20314,pri,every_pri,any_pri,pri_keys,sec,every_sec,any_sec,sec_keys,vec__20315,every_pri__$1,pri_keys__$1,vec__20316,every_sec__$1,sec_keys__$1,valid_pri_QMARK_,valid_sec_QMARK_,map__20312,map__20312__$1,opts,any_row,every_row,any_col,every_col,keys_only,vals_only,by_rows,by_cols))
;
return iter__19348__auto__.call(null,pri_keys__$1);
});
